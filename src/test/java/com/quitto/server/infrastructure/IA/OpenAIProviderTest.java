package com.quitto.server.infrastructure.IA;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.quitto.server.domain.enums.IA.AIProviderType;

/**
 * Testes unitários do {@link OpenAIProvider}.
 *
 * <p>Verifica identidade do provedor (enum, nome, URL base, envId), o catálogo
 * vazio por padrão, o hook de fetch ainda não implementado e o ciclo de
 * vida/configuração herdado do {@link BaseProvider}.</p>
 */
class OpenAIProviderTest {

    private final OpenAIProvider provider = new OpenAIProvider();

    @Test
    void shouldExposeOpenAIIdentity() {
        assertEquals(AIProviderType.OPENAI, provider.getProvider());
        assertEquals("OpenAI", provider.getName());
        assertEquals("https://api.openai.com/v1", provider.getApiBaseURL());
        assertEquals("OPENAI", provider.getEnvId());
    }

    @Test
    void shouldReturnEmptyModelCatalog() {
        assertEquals(List.of(), provider.getModels());
    }

    @Test
    void shouldRejectFetchModelsAsNotImplemented() {
        UnsupportedOperationException ex = assertThrows(
                UnsupportedOperationException.class,
                () -> provider.fetchModelsFromApi());
        assertTrue(ex.getMessage().contains("OpenAI"));
    }

    @Test
    void shouldStartEnabledAndToggle() {
        assertTrue(provider.isEnabled());
        provider.turnOff();
        assertFalse(provider.isEnabled());
        provider.turnOn();
        assertTrue(provider.isEnabled());
    }

    @Test
    void shouldRequireKeyAndTrackConfiguration() {
        assertTrue(provider.requiresKey());
        assertFalse(provider.isConfigured());

        provider.setKey("sk-openai");
        assertTrue(provider.isConfigured());

        provider.setKey(" ");
        assertFalse(provider.isConfigured());
    }
}