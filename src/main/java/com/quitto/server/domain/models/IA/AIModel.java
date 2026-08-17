package com.quitto.server.domain.models.IA;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

import com.quitto.server.domain.enums.IA.AIModelCapability;
import com.quitto.server.domain.enums.IA.AIProviderType;

/**
 * Representa um modelo de IA oferecido por um provedor.
 *
 * <p>É a unidade do catálogo de IA: cada instância identifica um modelo
 * concreto (ex.: {@code gpt-4o} da OpenAI) pelo {@code providerModelId} e
 * descreve suas capacidades ({@link AIModelCapability}) e preço
 * ({@link AIModelPricing}). A relação é 1:N — um provedor
 * ({@link AIProviderType}) expõe vários modelos via
 * {@code AIProvider.getModels()}.</p>
 *
 * <p><strong>Imutabilidade:</strong> a classe é {@code final} e todos os
 * campos são {@code final}. O conjunto de capacidades é copiado para um
 * {@code EnumSet} imutável na construção — mutações externas não afetam a
 * instância. Preço nulo é normalizado para {@link AIModelPricing#FREE}.</p>
 */
public final class AIModel {

    private final String providerModelId;
    private final String name;
    private final AIProviderType provider;
    private final Set<AIModelCapability> capabilities;
    private final AIModelPricing pricing;

    public AIModel(
            String providerModelId,
            String name,
            AIProviderType provider,
            Set<AIModelCapability> capabilities,
            AIModelPricing pricing) {

        this.providerModelId = Objects.requireNonNull(providerModelId, "providerModelId must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.provider = Objects.requireNonNull(provider, "provider must not be null");

        Objects.requireNonNull(capabilities, "capabilities must not be null");
        this.capabilities = Collections.unmodifiableSet(
                capabilities.isEmpty()
                        ? EnumSet.noneOf(AIModelCapability.class)
                        : EnumSet.copyOf(capabilities));

        this.pricing = pricing == null ? AIModelPricing.FREE : pricing;
    }

    public String getId() {
        return providerModelId;
    }

    public AIProviderType getProvider() {
        return provider;
    }

    public String getName() {
        return name;
    }

    public Set<AIModelCapability> getCapabilities() {
        return capabilities;
    }

    public AIModelPricing getPricing() {
        return pricing;
    }

    public boolean supportsStreaming() {
        return capabilities.contains(AIModelCapability.STREAMING);
    }

    public boolean supportsTools() {
        return capabilities.contains(AIModelCapability.TOOLS);
    }

    public boolean supportsReasoning() {
        return capabilities.contains(AIModelCapability.REASONING);
    }
}
