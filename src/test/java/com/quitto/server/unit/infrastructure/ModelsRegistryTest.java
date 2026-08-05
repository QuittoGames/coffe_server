package com.quitto.server.unit.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.quitto.server.domain.enums.ServiceProvider;
import com.quitto.server.domain.interfaces.IA.AIProvider;
import com.quitto.server.domain.interfaces.IA.ModelInfo;
import com.quitto.server.domain.models.IA.Model;
import com.quitto.server.infrastructure.services.IA.Models.Registry.ModelsRegistry;
import com.quitto.server.infrastructure.services.IA.Provaiders.Reagistry.AIProviderRegistry;

@ExtendWith(MockitoExtension.class)
@DisplayName("ModelsRegistry Tests")
class ModelsRegistryTest {

    @Mock
    private AIProviderRegistry providerRegistry;

    @Mock
    private AIProvider openAiProvider;

    @Mock
    private AIProvider anthropicProvider;

    private Model gpt4o;
    private Model gpt4Turbo;
    private Model claude3;

    private ModelsRegistry registry;

    @BeforeEach
    void setUp() {
        gpt4o = model("gpt-4o", "openai/gpt-4o", ServiceProvider.OPENAI);
        gpt4Turbo = model("gpt-4-turbo", "openai/gpt-4-turbo", ServiceProvider.OPENAI);
        claude3 = model("claude-3-opus", "anthropic/claude-3-opus", ServiceProvider.ANTHROPIC);

        registry = new ModelsRegistry(providerRegistry);
    }

    private Model model(String id, String providerModelId, ServiceProvider provider) {
        return new Model(
                new ModelInfo(id, 1L, provider.name()),
                provider,
                providerModelId,
                true,
                true,
                false);
    }

    @Test
    @DisplayName("find by provider returns the provider's list of models (1:N)")
    void find_byProvider_returnsProviderModelList() {
        registry.register(openAiProvider, gpt4o);
        registry.register(openAiProvider, gpt4Turbo);

        Optional<List<Model>> result = registry.find(openAiProvider);

        assertTrue(result.isPresent());
        assertEquals(List.of(gpt4o, gpt4Turbo), result.get());
    }

    @Test
    @DisplayName("find by provider separates models of different providers")
    void find_byProvider_separatesProviders() {
        registry.register(openAiProvider, gpt4o);
        registry.register(anthropicProvider, claude3);

        Optional<List<Model>> openAiModels = registry.find(openAiProvider);
        Optional<List<Model>> anthropicModels = registry.find(anthropicProvider);

        assertTrue(openAiModels.isPresent());
        assertEquals(List.of(gpt4o), openAiModels.get());
        assertTrue(anthropicModels.isPresent());
        assertEquals(List.of(claude3), anthropicModels.get());
    }

    @Test
    @DisplayName("find by unknown provider returns empty")
    void find_byUnknownProvider_returnsEmpty() {
        Optional<List<Model>> result = registry.find(anthropicProvider);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("find by name matches id or providerModelId, case-insensitive")
    void find_byName_matchesIdOrProviderModelId() {
        registry.register(openAiProvider, gpt4o);
        registry.register(anthropicProvider, claude3);

        Optional<List<Model>> byId = registry.find("GPT-4O");
        Optional<List<Model>> byProviderId = registry.find("anthropic/claude-3-opus");
        Optional<List<Model>> unknown = registry.find("nope");

        assertTrue(byId.isPresent());
        assertEquals(List.of(gpt4o), byId.get());
        assertTrue(byProviderId.isPresent());
        assertEquals(List.of(claude3), byProviderId.get());
        assertTrue(unknown.isEmpty());
    }

    @Test
    @DisplayName("getAll flattens models from all providers")
    void getAll_flattensAllProviders() {
        registry.register(openAiProvider, gpt4o);
        registry.register(openAiProvider, gpt4Turbo);
        registry.register(anthropicProvider, claude3);

        List<Model> all = registry.getAll();

        assertEquals(3, all.size());
        assertTrue(all.containsAll(List.of(gpt4o, gpt4Turbo, claude3)));
    }

    @Test
    @DisplayName("batch register adds a list of models to a provider")
    void register_batchAddsModelList() {
        registry.register(openAiProvider, List.of(gpt4o, gpt4Turbo));

        Optional<List<Model>> result = registry.find(openAiProvider);

        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        assertEquals(2, registry.size());
    }

    @Test
    @DisplayName("size counts all models across providers")
    void size_countsAllModels() {
        assertEquals(0, registry.size());

        registry.register(openAiProvider, gpt4o);
        registry.register(openAiProvider, gpt4Turbo);
        registry.register(anthropicProvider, claude3);

        assertEquals(3, registry.size());
    }

    @Test
    @DisplayName("containsKey and containsName")
    void containsKeyAndContainsName() {
        registry.register(openAiProvider, gpt4o);

        assertTrue(registry.containsKey(openAiProvider));
        assertFalse(registry.containsKey(anthropicProvider));

        assertTrue(registry.containsName("gpt-4o"));
        assertFalse(registry.containsName("claude-3-opus"));
    }
}
