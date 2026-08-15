package com.quitto.server.infrastructure.services.IA.Provaiders.Reagistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.quitto.server.domain.enums.ServiceProvider;
import com.quitto.server.domain.exception.ProviderException;
import com.quitto.server.domain.interfaces.IA.AIProvider;
import com.quitto.server.domain.interfaces.IA.AIRegistry;
import com.quitto.server.domain.models.IA.AIModel;
import com.quitto.server.infrastructure.services.CoffeAgent.CoffeAgentService;
import com.quitto.server.shared.exception.NotEnableExceptions;

@Service
public class AIProviderRegistry implements AIRegistry<ServiceProvider, AIProvider, AIModel> {

    private static final Logger log = LoggerFactory.getLogger(AIProviderRegistry.class);

    private final Map<ServiceProvider, AIProvider> registry;

    private final CoffeAgentService agentService;

    private final int maxProvidersPerRequest;

    public AIProviderRegistry(
            List<AIProvider> providers,
            CoffeAgentService agentService,
            @Value("${coffee.ai.max-providers-per-request:20}") int maxProvidersPerRequest) {
        this.agentService = Objects.requireNonNull(agentService, "agentService cannot be null");
        this.maxProvidersPerRequest = maxProvidersPerRequest;
        this.registry = load(providers);
    }

    private Map<ServiceProvider, AIProvider> load(List<AIProvider> providers) {
        Map<ServiceProvider, AIProvider> map = providers.stream()
                .collect(Collectors.toMap(AIProvider::getProvider, Function.identity()));

        map.values().forEach(this::ensureKeyIsSet);
        log.debug("Load Providaders of Implemetateion Classes");
        return map;
    }

    /**
     * Garante que a chave de API do provedor esteja resolvida. A chave é lida do
     * ambiente apenas quando ausente; provedores que exigem chave e não têm chave
     * configurada são desabilitados (D3).
     */
    private void ensureKeyIsSet(AIProvider provider) {
        if (provider.getApiKey() == null || provider.getApiKey().isBlank()) {
            String providerName = provider.getProvider().name();
            String envId = provider.getEnvId();
            // Usa o envId se disponível, caso contrário usa o nome do provedor

            String keyLookupValue = (envId != null && !envId.isBlank()) ? envId : providerName;
            agentService.getEnvKey(keyLookupValue).ifPresentOrElse(
                    provider::setKey,
                    () -> {
                        if (provider.requiresKey()) {
                            log.warn("API key não configurada para '{}' (env: COFFEE_AI_{}_KEY) — provedor desabilitado",
                                    provider.getProvider(), keyLookupValue);
                            provider.turnOff();
                        }
                    });
        }
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
        ensureKeyIsSet(providerTools);
        return providerTools.getModels();
    }

    @Override
    public List<AIModel> getAllModels() {
        List<AIModel> models = new ArrayList<>();

        int collected = 0;
        for (AIProvider provider : registry.values()) {
            if (collected >= maxProvidersPerRequest) {
                log.warn("Limite de {} provedores por requisição atingido — provedores restantes ignorados no catálogo",
                        maxProvidersPerRequest);
                break;
            }
            if (!provider.isEnabled()) {
                continue;
            }
            try {
                ensureKeyIsSet(provider);
                models.addAll(provider.getModels());
                collected++;
            } catch (ProviderException e) {
                // Falha de um provedor não derruba o catálogo inteiro (F2)
                log.warn("Falha ao listar modelos do provedor '{}': {}", provider.getProvider(), e.getMessage());
            }
        }
        return models;
    }

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
