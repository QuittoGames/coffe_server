package com.quitto.server.infrastructure.IA;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.quitto.server.domain.enums.IA.AIProviderType;

/**
 * Testes unitários do {@link BaseProvider} — a base abstrata dos provedores de
 * IA da infraestrutura.
 *
 * <p>Verifica o contrato base da infraestrutura de IA: identidade, ciclo de
 * vida ({@code enabled}), configuração por chave, catálogo vazio por padrão e
 * o hook {@code fetchModelsFromApi()} que ainda não é implementado pelos
 * provedores atuais (TODO em todos os concretos).</p>
 */
class BaseProviderTest {

    /** Provider concreto mínimo para exercitar o contrato da base. */
    static final class TestProvider extends BaseProvider {

        @Override
        public AIProviderType getProvider() {
            return AIProviderType.OPENAI;
        }

        @Override
        public String getName() {
            return "TestProvider";
        }

        @Override
        public String getApiBaseURL() {
            return "https://test.example.com/v1";
        }
    }

    private final BaseProvider provider = new TestProvider();

    @Test
    void shouldExposeProviderIdentity() {
        assertEquals(AIProviderType.OPENAI, provider.getProvider());
        assertEquals("TestProvider", provider.getName());
        assertEquals("https://test.example.com/v1", provider.getApiBaseURL());
    }

    @Test
    void shouldReturnEmptyModelCatalogByDefault() {
        assertEquals(List.of(), provider.getModels());
    }

    @Test
    void shouldStartEnabled() {
        assertTrue(provider.isEnabled());
    }

    @Test
    void shouldToggleEnabled() {
        provider.turnOff();
        assertFalse(provider.isEnabled());

        provider.turnOn();
        assertTrue(provider.isEnabled());
    }

    @Test
    void shouldRejectFetchModelsAsNotImplemented() {
        UnsupportedOperationException ex = assertThrows(
                UnsupportedOperationException.class,
                () -> provider.fetchModelsFromApi());
        assertTrue(ex.getMessage().contains("TestProvider"));
    }

    @Test
    void shouldRequireKeyByDefault() {
        assertTrue(provider.requiresKey());
    }

    @Test
    void shouldBeUnconfiguredWithoutKey() {
        assertFalse(provider.isConfigured());
    }

    @Test
    void shouldBeConfiguredAfterSetKey() {
        provider.setKey("sk-test");
        assertTrue(provider.isConfigured());
    }

    @Test
    void shouldBeUnconfiguredWhenKeyIsBlank() {
        provider.setKey("   ");
        assertFalse(provider.isConfigured());
    }

    @Test
    void shouldHaveNullEnvIdByDefault() {
        assertNull(provider.getEnvId());
    }

    @Test
    void shouldSetEnvId() {
        provider.setEnvId("OPENAI");
        assertEquals("OPENAI", provider.getEnvId());
    }
}