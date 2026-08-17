package com.quitto.server.application.services.IA;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.quitto.server.application.interfaces.IA.AIProviderPort;
import com.quitto.server.domain.enums.IA.AIProviderType;
import com.quitto.server.domain.interfaces.IA.AIProvider;
import com.quitto.server.domain.interfaces.IA.AIProviderRegistry;
import com.quitto.server.domain.models.IA.AIModel;

/**
 * Use case de consulta ao ecossistema de provedores de IA.
 *
 * <p>Orquestra o {@link AIProviderRegistry} do domínio para expor o catálogo
 * de providers e modelos à camada REST. Nenhuma regra de negócio vive aqui —
 * apenas orquestração e tradução de entradas (string → {@link AIProviderType}).</p>
 */
@Service
public class ProviderService implements AIProviderPort {

    private static final Logger log = LoggerFactory.getLogger(ProviderService.class);

    private final AIProviderRegistry registry;

    public ProviderService(AIProviderRegistry registry) {
        this.registry = registry;
    }

    @Override
    public List<AIProvider> getAllProviders() {
        return registry.getAll();
    }

    @Override
    public List<AIModel> getAllModels() {
        return registry.getAll().stream()
                .flatMap(provider -> provider.getModels().stream())
                .toList();
    }

    @Override
    public AIModel getModel(String modelId) {
        return registry.getAll().stream()
                .flatMap(provider -> provider.getModels().stream())
                .filter(model -> model.getId().equals(modelId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Model not found: " + modelId));
    }

    @Override
    public AIProvider findProvider(String providerString) {
        AIProviderType type = AIProviderType.valueOf(providerString.toUpperCase());
        AIProvider provider = registry.findOrThrow(type);
        log.debug("Provider resolved: {}", provider.getName());
        return provider;
    }
}