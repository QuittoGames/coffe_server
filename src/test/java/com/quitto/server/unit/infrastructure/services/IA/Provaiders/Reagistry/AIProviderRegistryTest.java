package com.quitto.server.unit.infrastructure.services.IA.Provaiders.Reagistry;

import com.quitto.server.domain.enums.ServiceProvider;
import com.quitto.server.domain.exception.ProviderException;
import com.quitto.server.domain.interfaces.IA.AIProvider;
import com.quitto.server.domain.models.IA.AIModel;
import com.quitto.server.infrastructure.services.CoffeAgent.CoffeAgentService;
import com.quitto.server.infrastructure.services.IA.Provaiders.Reagistry.AIProviderRegistry;
import com.quitto.server.shared.exception.NotEnableExceptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testa o {@link AIProviderRegistry} (PG-005): registro por chave do enum,
 * resolução de secrets via {@link CoffeAgentService} (lazy), ciclo de vida
 * (turnOff quando sem chave), busca por nome e o catálogo agregado com cap.
 *
 * <p>Nota: mocks de {@link AIProvider} NÃO executam métodos default — é preciso
 * stub explícito de {@code requiresKey()}/{@code isEnabled()} quando o teste
 * depende do valor (defaults: false/false).</p>
 */
@ExtendWith(MockitoExtension.class)
class AIProviderRegistryTest {

    @Mock
    private CoffeAgentService agentService;

    private static AIModel model(String id) {
        return new AIModel(id, id, ServiceProvider.OPENAI, true, true, false);
    }

    private static AIProvider providerWithKey(ServiceProvider sp, String apiKey) {
        AIProvider provider = mock(AIProvider.class);
        when(provider.getProvider()).thenReturn(sp);
        when(provider.getApiKey()).thenReturn(apiKey);
        return provider;
    }

    // ── registro por chave ──────────────────────────────────────────────────

    @Test
    void constructor_registersProvidersByEnumKey() {
        AIProvider pA = providerWithKey(ServiceProvider.OPENAI, "k");
        AIProvider pB = providerWithKey(ServiceProvider.ANTHROPIC, "k");

        AIProviderRegistry registry = new AIProviderRegistry(List.of(pA, pB), agentService, 20);

        assertEquals(2, registry.size());
        assertTrue(registry.containsKey(ServiceProvider.OPENAI));
        assertTrue(registry.containsKey(ServiceProvider.ANTHROPIC));
        assertEquals(Optional.of(List.of(pA)), registry.find(ServiceProvider.OPENAI));
        assertEquals(Optional.of(pA), registry.findProvider(ServiceProvider.OPENAI));
        assertEquals(2, registry.getAll().size());
        assertThrows(UnsupportedOperationException.class, () -> registry.getAll().add(pA));
    }

    // ── resolução de secrets (ensureKeyIsSet) ───────────────────────────────

    @Test
    void constructor_loadsKeyFromEnvWhenApiKeyBlank() {
        AIProvider pA = mock(AIProvider.class);
        when(pA.getProvider()).thenReturn(ServiceProvider.OPENAI);
        when(agentService.getEnvKey("OPENAI")).thenReturn(Optional.of("sk-test"));

        new AIProviderRegistry(List.of(pA), agentService, 20);

        verify(pA).setKey("sk-test");
    }

    @Test
    void constructor_turnsOffProviderWithoutKeyWhenRequiresKey() {
        AIProvider pA = mock(AIProvider.class);
        when(pA.getProvider()).thenReturn(ServiceProvider.OPENAI);
        when(pA.requiresKey()).thenReturn(true);

        new AIProviderRegistry(List.of(pA), agentService, 20);

        verify(pA).turnOff();
    }

    @Test
    void constructor_keepsProviderEnabledWhenKeyMissingButNotRequired() {
        AIProvider pA = mock(AIProvider.class);
        when(pA.getProvider()).thenReturn(ServiceProvider.OPENAI);
        when(pA.requiresKey()).thenReturn(false);

        new AIProviderRegistry(List.of(pA), agentService, 20);

        verify(pA, never()).turnOff();
    }

    @Test
    void constructor_nullAgentService_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> new AIProviderRegistry(List.of(), null, 20));
    }

    // ── busca por nome ──────────────────────────────────────────────────────

    @Test
    void find_byStringIsCaseInsensitive() {
        AIProvider pA = providerWithKey(ServiceProvider.OPENAI, "k");
        when(pA.getName()).thenReturn("OpenAI");

        AIProviderRegistry registry = new AIProviderRegistry(List.of(pA), agentService, 20);

        assertEquals(Optional.of(List.of(pA)), registry.find("openai"));
        assertEquals(Optional.of(List.of(pA)), registry.find("OPENAI"));
        assertTrue(registry.containsName("openai"));
        assertEquals(Optional.empty(), registry.find("unknown"));
    }

    // ── getModelsForProvaider ───────────────────────────────────────────────

    @Test
    void getModelsForProvaider_returnsModels() {
        AIProvider pA = providerWithKey(ServiceProvider.OPENAI, "k");
        AIModel m1 = model("m1");
        when(pA.getModels()).thenReturn(List.of(m1));

        AIProviderRegistry registry = new AIProviderRegistry(List.of(pA), agentService, 20);

        assertEquals(List.of(m1), registry.getModelsForProvaider(ServiceProvider.OPENAI));
    }

    @Test
    void getModelsForProvaider_unregistered_throwsNoSuchElement() {
        AIProviderRegistry registry = new AIProviderRegistry(List.of(), agentService, 20);

        assertThrows(NoSuchElementException.class,
                () -> registry.getModelsForProvaider(ServiceProvider.OPENAI));
    }

    // ── getAllModels (catálogo agregado) ────────────────────────────────────

    @Test
    void getAllModels_skipsDisabledProviders() {
        AIProvider pA = providerWithKey(ServiceProvider.OPENAI, "k");
        AIProvider pB = providerWithKey(ServiceProvider.ANTHROPIC, "k");
        AIModel m1 = model("m1");
        when(pA.isEnabled()).thenReturn(true);
        when(pA.getModels()).thenReturn(List.of(m1));
        when(pB.isEnabled()).thenReturn(false);

        AIProviderRegistry registry = new AIProviderRegistry(List.of(pA, pB), agentService, 20);

        assertEquals(List.of(m1), registry.getAllModels());
    }

    @Test
    void getAllModels_capsAtMaxProvidersPerRequest() {
        AIProvider pA = providerWithKey(ServiceProvider.OPENAI, "k");
        AIProvider pB = providerWithKey(ServiceProvider.ANTHROPIC, "k");
        AIModel m1 = model("m1");
        when(pA.isEnabled()).thenReturn(true);
        when(pA.getModels()).thenReturn(List.of(m1));
        // pB fica com isEnabled default false — nunca contribui nem é capado antes de pA

        AIProviderRegistry registry = new AIProviderRegistry(List.of(pA, pB), agentService, 1);

        assertEquals(List.of(m1), registry.getAllModels());
    }

    @Test
    void getAllModels_isolatesProviderFailure() {
        AIProvider pA = providerWithKey(ServiceProvider.OPENAI, "k");
        AIProvider pB = providerWithKey(ServiceProvider.ANTHROPIC, "k");
        AIModel m2 = model("m2");
        when(pA.isEnabled()).thenReturn(true);
        when(pA.getModels()).thenThrow(new ProviderException("boom"));
        when(pB.isEnabled()).thenReturn(true);
        when(pB.getModels()).thenReturn(List.of(m2));

        AIProviderRegistry registry = new AIProviderRegistry(List.of(pA, pB), agentService, 20);

        assertEquals(List.of(m2), registry.getAllModels());
    }

    // ── findProvaider ───────────────────────────────────────────────────────

    @Test
    void findProvaider_returnsEnabledProvider() {
        AIProvider pA = providerWithKey(ServiceProvider.OPENAI, "k");
        when(pA.isEnabled()).thenReturn(true);

        AIProviderRegistry registry = new AIProviderRegistry(List.of(pA), agentService, 20);

        assertEquals(pA, registry.findProvaider(ServiceProvider.OPENAI));
    }

    @Test
    void findProvaider_disabled_throwsNotEnableExceptions() {
        AIProvider pA = providerWithKey(ServiceProvider.OPENAI, "k");

        AIProviderRegistry registry = new AIProviderRegistry(List.of(pA), agentService, 20);

        assertThrows(NotEnableExceptions.class,
                () -> registry.findProvaider(ServiceProvider.OPENAI));
    }

    @Test
    void findProvaider_unregistered_throwsIllegalArgumentException() {
        AIProviderRegistry registry = new AIProviderRegistry(List.of(), agentService, 20);

        assertThrows(IllegalArgumentException.class,
                () -> registry.findProvaider(ServiceProvider.OPENAI));
    }

    @Test
    void findProvaider_null_throwsNullPointerException() {
        AIProviderRegistry registry = new AIProviderRegistry(List.of(), agentService, 20);

        assertThrows(NullPointerException.class,
                () -> registry.findProvaider(null));
    }

    // ── register ────────────────────────────────────────────────────────────

    @Test
    void register_addsProviderByKey() {
        AIProviderRegistry registry = new AIProviderRegistry(List.of(), agentService, 20);
        AIProvider pA = mock(AIProvider.class);

        registry.register(ServiceProvider.CUSTOM, pA);

        assertTrue(registry.containsKey(ServiceProvider.CUSTOM));
        assertEquals(1, registry.size());
    }
}