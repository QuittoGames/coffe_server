package com.quitto.server.unit.infrastructure.services.IA;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.EnumSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.quitto.server.application.services.IA.ProviderService;
import com.quitto.server.domain.enums.IA.AIModelCapability;
import com.quitto.server.domain.enums.IA.AIProviderType;
import com.quitto.server.domain.exception.ProviderNotFoundException;
import com.quitto.server.domain.interfaces.IA.AIProvider;
import com.quitto.server.domain.interfaces.IA.AIProviderRegistry;
import com.quitto.server.domain.models.IA.AIModel;
import com.quitto.server.domain.models.IA.AIModelPricing;

/**
 * Testes unitários do {@link ProviderService} (use case de IA) com mocks das
 * portas do domínio — sem HTTP, sem Spring context.
 */
@ExtendWith(MockitoExtension.class)
class ProviderServiceTest {

    @Mock
    private AIProviderRegistry registry;

    @Mock
    private AIProvider provider;

    private AIModel model(String id, String name, AIProviderType type) {
        return new AIModel(id, name, type, EnumSet.of(AIModelCapability.STREAMING), AIModelPricing.FREE);
    }

    @Test
    void shouldDelegateGetAllProviders() {
        when(registry.getAll()).thenReturn(List.of(provider));

        List<AIProvider> all = new ProviderService(registry).getAllProviders();

        assertEquals(1, all.size());
        assertTrue(all.contains(provider));
    }

    @Test
    void shouldReturnEmptyModelsWhenNoProviders() {
        when(registry.getAll()).thenReturn(List.of());

        assertTrue(new ProviderService(registry).getAllModels().isEmpty());
    }

    @Test
    void shouldFlattenModelsFromAllProviders() {
        AIProvider second = mock(AIProvider.class);
        when(registry.getAll()).thenReturn(List.of(provider, second));

        AIModel gpt = model("gpt-4o", "GPT-4o", AIProviderType.OPENAI);
        AIModel mini = model("gpt-4o-mini", "GPT-4o mini", AIProviderType.OPENAI);
        AIModel claude = model("claude-3-5-sonnet", "Claude 3.5 Sonnet", AIProviderType.GEMINI);
        when(provider.getModels()).thenReturn(List.of(gpt, mini));
        when(second.getModels()).thenReturn(List.of(claude));

        List<AIModel> models = new ProviderService(registry).getAllModels();

        assertEquals(3, models.size());
        assertTrue(models.containsAll(List.of(gpt, mini, claude)));
    }

    @Test
    void shouldFindModelById() {
        AIModel gpt = model("gpt-4o", "GPT-4o", AIProviderType.OPENAI);
        AIModel mini = model("gpt-4o-mini", "GPT-4o mini", AIProviderType.OPENAI);
        when(registry.getAll()).thenReturn(List.of(provider));
        when(provider.getModels()).thenReturn(List.of(gpt, mini));

        assertSame(gpt, new ProviderService(registry).getModel("gpt-4o"));
    }

    @Test
    void shouldThrowWhenModelNotFound() {
        when(registry.getAll()).thenReturn(List.of(provider));
        when(provider.getModels()).thenReturn(List.of());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new ProviderService(registry).getModel("gpt-4o"));
        assertTrue(ex.getMessage().contains("gpt-4o"));
    }

    @Test
    void shouldFindProviderByNameCaseInsensitive() {
        when(registry.findOrThrow(AIProviderType.OPENAI)).thenReturn(provider);

        ProviderService service = new ProviderService(registry);

        assertSame(provider, service.findProvider("openai"));
        assertSame(provider, service.findProvider("OPENAI"));
    }

    @Test
    void shouldThrowWhenProviderNameIsNotAType() {
        assertThrows(IllegalArgumentException.class, () -> new ProviderService(registry).findProvider("unknown"));
    }

    @Test
    void shouldPropagateProviderNotFoundFromRegistry() {
        when(registry.findOrThrow(AIProviderType.GEMINI))
                .thenThrow(new ProviderNotFoundException("AI provider not found: GEMINI"));

        assertThrows(
                ProviderNotFoundException.class,
                () -> new ProviderService(registry).findProvider("gemini"));
    }
}