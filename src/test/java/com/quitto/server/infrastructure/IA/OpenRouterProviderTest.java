package com.quitto.server.infrastructure.IA;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.quitto.server.domain.enums.IA.AIProviderType;

/**
 * Testes unitários do {@link OpenRouterProvider}.
 *
 * <p>Verifica identidade do provedor (enum, nome, URL base, envId), o catálogo
 * vazio por padrão, o hook de fetch ainda não implementado e o ciclo de
 * vida/configuração herdado do {@link BaseProvider}.</p>
 */
class OpenRouterProviderTest {

    private final OpenRouterProvider provider = new OpenRouterProvider();

    @Test
    void shouldExposeOpenRouterIdentity() {
        assertEquals(AIProviderType.OPENROUTER, provider.getProvider());
        assertEquals("OpenRouter", provider.getName());
        assertEquals("https://openrouter.ai/api/v1", provider.getApiBaseURL());
        assertEquals("OPENROUTER", provider.getEnvId());
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
        assertTrue(ex.getMessage().contains("OpenRouter"));
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

        provider.setKey("sk-openrouter");
        assertTrue(provider.isConfigured());

        provider.setKey(" ");
        assertFalse(provider.isConfigured());
    }
}