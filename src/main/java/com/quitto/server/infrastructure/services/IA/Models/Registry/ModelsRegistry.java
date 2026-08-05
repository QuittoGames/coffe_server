package com.quitto.server.infrastructure.services.IA.Models.Registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.quitto.server.domain.interfaces.IA.AIProvider;
import com.quitto.server.domain.interfaces.IA.AIRegistry;
import com.quitto.server.domain.models.IA.Model;
import com.quitto.server.infrastructure.services.IA.Provaiders.Reagistry.AIProviderRegistry;

/**
 * Registro dos modelos de IA do escopo, indexado por {@link AIProvider}.
 *
 * <p>Um provider expõe uma <b>lista</b> de modelos (via
 * {@link AIProvider#getModels()}), por isso o catálogo é 1:N: cada chave
 * {@link AIProvider} aponta para os modelos registrados daquele provedor.
 * O catálogo começa vazio; a população por provedor deve ser feita de forma
 * lazy, apenas para provedores habilitados e com chave configurada.</p>
 */
@Service
public class ModelsRegistry implements AIRegistry<AIProvider, Model> {

    private final Map<AIProvider, List<Model>> modelsRegistry = new HashMap<>();

    private final AIProviderRegistry providerRegistry;

    public ModelsRegistry(AIProviderRegistry providerRegistry) {

        this.providerRegistry = Objects.requireNonNull(providerRegistry);
    }

    @Override
    public Optional<List<Model>> find(AIProvider provider) {

        return Optional.ofNullable(modelsRegistry.get(provider));
    }

    @Override
    public Optional<List<Model>> find(String name) {

        List<Model> matches = getAll()
                .stream()
                .filter(model ->
                        model.getId().equalsIgnoreCase(name) ||
                        model.getProviderModelId().equalsIgnoreCase(name))
                .toList();

        return matches.isEmpty() ? Optional.empty() : Optional.of(matches);
    }

    @Override
    public void register(AIProvider provider, Model model) {

        modelsRegistry
                .computeIfAbsent(provider, p -> new ArrayList<>())
                .add(model);
    }

    public void register(AIProvider provider, List<Model> models) {

        modelsRegistry
                .computeIfAbsent(provider, p -> new ArrayList<>())
                .addAll(models);
    }

    @Override
    public List<Model> getAll() {

        return modelsRegistry.values()
                .stream()
                .flatMap(List::stream)
                .toList();
    }

    @Override
    public int size() {

        return modelsRegistry.values()
                .stream()
                .mapToInt(List::size)
                .sum();
    }

    @Override
    public boolean containsKey(AIProvider provider) {

        return modelsRegistry.containsKey(provider);
    }

    @Override
    public boolean containsName(String name) {

        return find(name).isPresent();
    }

    public static void sync(){

    }
}
