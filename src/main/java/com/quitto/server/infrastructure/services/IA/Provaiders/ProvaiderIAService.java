package com.quitto.server.infrastructure.services.IA.Provaiders;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.quitto.server.domain.enums.ServiceProvider;
import com.quitto.server.domain.interfaces.IA.AIProvider;
import com.quitto.server.domain.models.IA.AIModel;
import com.quitto.server.infrastructure.services.IA.Provaiders.Reagistry.AIProviderRegistry;

@Service
public class ProvaiderIAService {

    private final AIProviderRegistry registry;

    private final int maxProvidersPerRequest;

    public ProvaiderIAService(
            AIProviderRegistry registry,
            @Value("${coffee.ai.max-providers-per-request:20}") int maxProvidersPerRequest) {
        this.registry = registry;
        this.maxProvidersPerRequest = maxProvidersPerRequest;
    }

    /**
     * Lista provedores habilitados, limitado ao teto por requisição (F2).
     * <p>Comportamento (desvio documentado): provedores desabilitados não
     * aparecem em {@code /provaiders} — o filtro evita fan-out de provedores
     * sem chave que foram desligados no boot (D3).</p>
     */
    public List<AIProvider> getAllProviders() {
        return registry.getAll().stream()
                .filter(AIProvider::isEnabled)
                .limit(maxProvidersPerRequest)
                .toList();
    }

    public List<AIModel> getAllModels() {
        return registry.getAllModels();
    }

    public AIModel getModel(String modelId) {
        return registry.getAllModels().stream()
                .filter(model -> model.getId().equalsIgnoreCase(modelId) || model.getName().equalsIgnoreCase(modelId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Model not found: " + modelId));
    }

    public AIProvider findProvider(ServiceProvider provider) {
        return registry.findProvaider(provider);
    }
}
