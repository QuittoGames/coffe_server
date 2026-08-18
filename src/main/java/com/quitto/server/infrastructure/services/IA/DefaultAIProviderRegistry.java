package com.quitto.server.infrastructure.services.IA;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.quitto.server.domain.enums.IA.AIProviderType;
import com.quitto.server.domain.exception.IA.ProviderNotFoundException;
import com.quitto.server.domain.interfaces.IA.AIProvider;
import com.quitto.server.domain.interfaces.IA.AIProviderRegistry;

/**
 * Registro padrão de provedores de IA (implementação de infraestrutura).
 *
 * <p>Indexa os {@link AIProvider} beans Spring pela chave
 * {@link AIProviderType} (1:1 — um provider por tipo). O Spring injeta a lista
 * de todos os {@code AIProvider} no construtor e o mapa imutável é montado na
 * carga: {@code AIProvider::getProvider} como chave.</p>
 *
 * <p>Implementa a porta de domínio {@link AIProviderRegistry} — o domínio não
 * conhece Spring nem a implementação concreta.</p>
 */
@Service
public class DefaultAIProviderRegistry implements AIProviderRegistry {

    private final Map<AIProviderType, AIProvider> registry;

    public DefaultAIProviderRegistry(List<AIProvider> providers) {
        this.registry = providers.stream()
                .collect(Collectors.toUnmodifiableMap(
                        AIProvider::getProvider,
                        Function.identity()));
    }

    @Override
    public List<AIProvider> getAll() {
        return List.copyOf(registry.values());
    }

    @Override
    public Optional<AIProvider> find(AIProviderType type) {
        if (type == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(registry.get(type));
    }

    @Override
    public AIProvider findOrThrow(AIProviderType type) {
        if (type == null) {
            throw new ProviderNotFoundException("AI provider not found: null");
        }
        AIProvider provider = registry.get(type);
        if (provider == null) {
            throw new ProviderNotFoundException("AI provider not found: " + type);
        }
        return provider;
    }

    @Override
    public boolean contains(AIProviderType type) {
        return type != null && registry.containsKey(type);
    }

    @Override
    public int size() {
        return registry.size();
    }
}