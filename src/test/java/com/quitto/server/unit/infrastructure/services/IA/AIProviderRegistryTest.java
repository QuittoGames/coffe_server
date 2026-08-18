package com.quitto.server.unit.infrastructure.services.IA;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.quitto.server.domain.enums.IA.AIProviderType;
import com.quitto.server.domain.exception.IA.ProviderNotFoundException;
import com.quitto.server.domain.interfaces.IA.AIProvider;
import com.quitto.server.infrastructure.services.IA.DefaultAIProviderRegistry;

/**
 * Testes unitários do {@link DefaultAIProviderRegistry}.
 *
 * <p>Verifica a indexação 1:1 por {@link AIProviderType}, as buscas
 * ({@code find} / {@code findOrThrow}), a imutabilidade de {@code getAll()} e
 * o tratamento de chave duplicada e registro vazio.</p>
 */
class AIProviderRegistryTest {

    private AIProvider providerOf(AIProviderType type) {
        AIProvider provider = mock(AIProvider.class);
        when(provider.getProvider()).thenReturn(type);
        return provider;
    }

    @Test
    void shouldIndexProvidersByType() {
        DefaultAIProviderRegistry registry = new DefaultAIProviderRegistry(
                List.of(providerOf(AIProviderType.OPENAI), providerOf(AIProviderType.OPENROUTER)));

        assertEquals(2, registry.size());
        assertTrue(registry.contains(AIProviderType.OPENAI));
        assertTrue(registry.contains(AIProviderType.OPENROUTER));
        assertFalse(registry.contains(AIProviderType.GEMINI));
    }

    @Test
    void shouldFindProviderByType() {
        AIProvider openai = providerOf(AIProviderType.OPENAI);
        DefaultAIProviderRegistry registry = new DefaultAIProviderRegistry(List.of(openai));

        assertTrue(registry.find(AIProviderType.OPENAI).isPresent());
        assertSame(openai, registry.find(AIProviderType.OPENAI).orElseThrow());
        assertTrue(registry.find(AIProviderType.GEMINI).isEmpty());
        assertTrue(registry.find(null).isEmpty());
    }

    @Test
    void shouldFindOrThrowProviderByType() {
        AIProvider openai = providerOf(AIProviderType.OPENAI);
        DefaultAIProviderRegistry registry = new DefaultAIProviderRegistry(List.of(openai));

        assertSame(openai, registry.findOrThrow(AIProviderType.OPENAI));

        ProviderNotFoundException ex = assertThrows(
                ProviderNotFoundException.class,
                () -> registry.findOrThrow(AIProviderType.GEMINI));
        assertTrue(ex.getMessage().contains("GEMINI"));
    }

    @Test
    void shouldReturnAllProvidersAsUnmodifiableList() {
        AIProvider openai = providerOf(AIProviderType.OPENAI);
        AIProvider openrouter = providerOf(AIProviderType.OPENROUTER);
        DefaultAIProviderRegistry registry = new DefaultAIProviderRegistry(List.of(openai, openrouter));

        List<AIProvider> all = registry.getAll();
        assertEquals(2, all.size());
        assertTrue(all.containsAll(List.of(openai, openrouter)));

        assertThrows(UnsupportedOperationException.class, () -> all.add(mock(AIProvider.class)));
    }

    @Test
    void shouldRejectDuplicateProviderType() {
        assertThrows(
                IllegalStateException.class,
                () -> new DefaultAIProviderRegistry(
                        List.of(providerOf(AIProviderType.OPENAI), providerOf(AIProviderType.OPENAI))));
    }

    @Test
    void shouldSupportEmptyRegistry() {
        DefaultAIProviderRegistry registry = new DefaultAIProviderRegistry(List.of());

        assertEquals(0, registry.size());
        assertTrue(registry.getAll().isEmpty());
        assertTrue(registry.find(AIProviderType.OPENAI).isEmpty());
        assertThrows(ProviderNotFoundException.class, () -> registry.findOrThrow(AIProviderType.OPENAI));
    }
}