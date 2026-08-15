package com.quitto.server.infrastructure.IA;

import com.fasterxml.jackson.databind.JsonNode;
import com.quitto.server.domain.enums.ServiceProvider;
import com.quitto.server.domain.exception.ProviderException;
import com.quitto.server.domain.models.IA.AIModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa o parsing de catálogo de modelos do {@link BaseProvider} (PG-005).
 *
 * <p>Cobre as raízes suportadas ({@code data}/{@code models}/{@code result}/
 * {@code resources}/{@code results}/array raiz), fallbacks de identidade
 * ({@code id} → {@code name} → {@code model_id} → {@code model_name}) e os
 * defaults de capacidade (stream=true, tools=true, reasoning=false).</p>
 */
class BaseProviderParsingTest {

    /** Provedor concreto mínimo para exercitar os hooks do {@link BaseProvider}. */
    private static final class TestProvider extends BaseProvider {
        private final String baseUrl;

        TestProvider(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        @Override
        public ServiceProvider getProvider() {
            return ServiceProvider.OPENAI;
        }

        @Override
        public String getName() {
            return "TestProvider";
        }

        @Override
        public String getApiBaseURL() {
            return baseUrl;
        }
    }

    private static List<AIModel> parse(TestProvider provider, String json) throws Exception {
        JsonNode root = provider.mapper().readTree(json);
        return provider.parseModels(root);
    }

    // ── raízes de resposta ──────────────────────────────────────────────────

    @Test
    void dataRoot_parsesModels() throws Exception {
        List<AIModel> models = parse(new TestProvider("https://example.com/v1"),
                "{\"data\":[{\"id\":\"gpt-4o\",\"name\":\"GPT-4o\"}]}");

        assertEquals(1, models.size());
        AIModel model = models.get(0);
        assertEquals("gpt-4o", model.getId());
        assertEquals("GPT-4o", model.getName());
        assertEquals(ServiceProvider.OPENAI, model.getProvider());
    }

    @Test
    void modelsRoot_parsesModels() throws Exception {
        List<AIModel> models = parse(new TestProvider("https://example.com/v1"),
                "{\"models\":[{\"id\":\"m1\",\"name\":\"M1\"}]}");

        assertEquals(1, models.size());
        assertEquals("m1", models.get(0).getId());
    }

    @Test
    void resultRoot_parsesModels() throws Exception {
        List<AIModel> models = parse(new TestProvider("https://example.com/v1"),
                "{\"result\":[{\"id\":\"r1\"}]}");

        assertEquals(1, models.size());
        assertEquals("r1", models.get(0).getId());
    }

    @Test
    void resourcesRoot_usesModelIdFallback() throws Exception {
        List<AIModel> models = parse(new TestProvider("https://example.com/v1"),
                "{\"resources\":[{\"model_id\":\"wx1\"}]}");

        assertEquals(1, models.size());
        assertEquals("wx1", models.get(0).getId());
        assertEquals("wx1", models.get(0).getName());
    }

    @Test
    void resultsRoot_parsesModels() throws Exception {
        List<AIModel> models = parse(new TestProvider("https://example.com/v1"),
                "{\"results\":[{\"id\":\"rep1\"}]}");

        assertEquals(1, models.size());
        assertEquals("rep1", models.get(0).getId());
    }

    @Test
    void rootArray_parsesAllModels() throws Exception {
        List<AIModel> models = parse(new TestProvider("https://example.com/v1"),
                "[{\"id\":\"a\"},{\"id\":\"b\"}]");

        assertEquals(2, models.size());
    }

    // ── precedência e fallbacks ─────────────────────────────────────────────

    @Test
    void dataRoot_takesPrecedenceOverModelsRoot() throws Exception {
        List<AIModel> models = parse(new TestProvider("https://example.com/v1"),
                "{\"data\":[{\"id\":\"d1\"}],\"models\":[{\"id\":\"m1\"}]}");

        assertEquals(1, models.size());
        assertEquals("d1", models.get(0).getId());
    }

    @Test
    void nodeWithoutIdentity_isIgnored() throws Exception {
        List<AIModel> models = parse(new TestProvider("https://example.com/v1"),
                "{\"data\":[{\"created\":123}]}");

        assertTrue(models.isEmpty());
    }

    @Test
    void emptyObject_returnsEmptyList() throws Exception {
        List<AIModel> models = parse(new TestProvider("https://example.com/v1"), "{}");

        assertTrue(models.isEmpty());
    }

    @Test
    void name_fallsBackToId() throws Exception {
        List<AIModel> models = parse(new TestProvider("https://example.com/v1"),
                "{\"data\":[{\"id\":\"only-id\"}]}");

        assertEquals(1, models.size());
        assertEquals("only-id", models.get(0).getName());
    }

    // ── capacidades default ─────────────────────────────────────────────────

    @Test
    void capabilities_defaultToStreamingToolsNotReasoning() throws Exception {
        List<AIModel> models = parse(new TestProvider("https://example.com/v1"),
                "{\"data\":[{\"id\":\"gpt-4o\"}]}");

        AIModel model = models.get(0);
        assertTrue(model.supportsStreaming());
        assertTrue(model.supportsTools());
        assertFalse(model.supportsReasoning());
    }

    // ── modelsUrl ───────────────────────────────────────────────────────────

    @Test
    void modelsUrl_appendsModelsPathToBase() {
        TestProvider provider = new TestProvider("https://example.com/v1");

        assertEquals("https://example.com/v1/models", provider.modelsUrl());
    }

    @Test
    void modelsUrl_stripsTrailingSlashFromBase() {
        TestProvider provider = new TestProvider("https://example.com/v1/");

        assertEquals("https://example.com/v1/models", provider.modelsUrl());
    }

    @Test
    void modelsUrl_nullBase_throwsProviderException() {
        TestProvider provider = new TestProvider(null);

        assertThrows(ProviderException.class, provider::modelsUrl);
    }

    // ── getModels sem chave ─────────────────────────────────────────────────

    @Test
    void getModels_withoutApiKey_throwsProviderException() {
        TestProvider provider = new TestProvider("https://example.com/v1");

        assertThrows(ProviderException.class, provider::getModels);
    }

    @Test
    void requiresKey_defaultsToTrue() {
        assertTrue(new TestProvider("https://example.com/v1").requiresKey());
    }
}