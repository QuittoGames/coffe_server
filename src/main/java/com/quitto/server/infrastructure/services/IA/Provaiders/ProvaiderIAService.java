package com.quitto.server.infrastructure.services.IA.Provaiders;

import java.util.List;

import org.springframework.stereotype.Service;

import com.quitto.server.domain.enums.ServiceProvider;
import com.quitto.server.domain.interfaces.IA.AIProvider;
import com.quitto.server.domain.interfaces.Serializer.Serializer;
import com.quitto.server.domain.models.IA.AIModel;
import com.quitto.server.infrastructure.services.IA.Provaiders.Reagistry.AIProviderRegistry;

@Service
public class ProvaiderIAService {
    private final AIProviderRegistry registry;
    private final Serializer serializer;

    public ProvaiderIAService(AIProviderRegistry registry, Serializer serializer) {
        this.registry = registry;
        this.serializer = serializer;
    }

    public List<AIProvider> getAllProviders() {
        return registry.getAll();
    }

    public List<AIModel> getAllModels() {
        return registry.getAllModels();
    }

    public AIModel getModel(String modelId) {
        return registry.getAllModels().stream()
                .filter(model -> model.getId().equalsIgnoreCase(modelId)
                        || model.getName().equalsIgnoreCase(modelId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No AI model found for: " + modelId));
    }

    public AIProvider findProvider(ServiceProvider provider) {
        return registry.findProvaider(provider);
    }

    public byte[] findProvaider(ServiceProvider provider) {
        return serializer.serialize(registry.findProvaider(provider));
    }
}
