package com.quitto.server.infrastructure.services.IA.Provaiders.Reagistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.quitto.server.domain.enums.ServiceProvider;
import com.quitto.server.domain.interfaces.IA.AIProvider;
import com.quitto.server.domain.interfaces.IA.AIRegistry;
import com.quitto.server.domain.models.IA.AIModel;
import com.quitto.server.infrastructure.services.CoffeAgent.CoffeAgentService;
import com.quitto.server.shared.exception.NotEnableExceptions;

/**
 * Registro dos provedores de IA do escopo, indexado por
 * {@link ServiceProvider}.
 *
 * <p>
 * No boot, injeta todos os beans {@link AIProvider}, aplica a chave de API
 * de cada um via {@link CoffeAgentService} e indexa pela enumeração.
 * </p>
 */
@Service
public class AIProviderRegistry implements AIRegistry<ServiceProvider, AIProvider, AIModel> {

    private final Map<ServiceProvider, AIProvider> registry;

    private final CoffeAgentService agentService;

    public AIProviderRegistry(List<AIProvider> providers, CoffeAgentService agentService) {
        this.agentService = Objects.requireNonNull(agentService, "agentService cannot be null");
        this.registry = load(providers);
    }

    private Map<ServiceProvider, AIProvider> load(List<AIProvider> providers) {
        providers.forEach(provider -> {
            String providerName = provider.getProvider().name();
            provider.setKey(agentService.getEnvKey(providerName));
        });
        return providers.stream()
                .collect(Collectors.toMap(
                        AIProvider::getProvider,
                        Function.identity()));
    }

    @Override
    public Optional<List<AIProvider>> find(ServiceProvider provider) {
        return Optional.ofNullable(registry.get(provider)).map(List::of);
    }

    @Override
    public Optional<List<AIProvider>> find(String name) {
        return registry.values().stream()
                .filter(provider -> provider.getProvider().name().equalsIgnoreCase(name)
                        || provider.getName().equalsIgnoreCase(name))
                .findFirst()
                .map(List::of);
    }

    /**
     * Busca um provedor pela enumeração.
     *
     * @param provider provedor desejado
     * @return o provedor registrado, ou vazio se não existir
     */
    @Override
    public Optional<AIProvider> findProvider(ServiceProvider provider) {
        Objects.requireNonNull(provider, "provider cannot be null");
        return Optional.ofNullable(registry.get(provider));
    }

    @Override
    public List<AIModel> getModelsForProvaider(ServiceProvider provider) {
        AIProvider providerTools = registry.get(provider);
        if (providerTools == null) {
            throw new NoSuchElementException(
                    "Provider " + provider + " is not registered.");
        }

        return providerTools.getModels();
    }

    @Override
    public List<AIModel> getAllModels() {
        List<AIModel> models = new ArrayList<>();
        for (AIProvider provider : registry.values()) {

            if (provider == null) {
                throw new NoSuchElementException(
                        "Provider " + provider + " is not registered.");
            }

            models.addAll(provider.getModels());
        }
        return models;

    }

    /**
     * Busca um provedor habilitado pela enumeração.
     *
     * @param provider provedor desejado
     * @return o provedor registrado e habilitado
     * @throws IllegalArgumentException se o provedor não estiver registrado
     * @throws NotEnableExceptions      se o provedor estiver desabilitado
     */
    public AIProvider findProvaider(ServiceProvider provider) throws IllegalArgumentException {
        Objects.requireNonNull(provider, "provider cannot be null");

        AIProvider data = findProvider(provider)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No AI provider registered for: " + provider));

        if (!data.isEnabled()) {
            throw new NotEnableExceptions("AI provider is disabled: " + provider);
        }

        return data;
    }

    @Override
    public List<AIProvider> getAll() {
        return List.copyOf(registry.values());
    }

    @Override
    public void register(ServiceProvider provider, AIProvider value) {
        registry.put(provider, value);
    }


    @Override
    public int size() {
        return registry.size();
    }

    @Override
    public boolean containsKey(ServiceProvider provider) {
        return registry.containsKey(provider);
    }

    @Override
    public boolean containsName(String name) {
        return find(name).isPresent();
    }
}
