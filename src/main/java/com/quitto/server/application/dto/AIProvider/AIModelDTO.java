package com.quitto.server.application.dto.AIProvider;

import java.util.Set;

import com.quitto.server.application.dto.BaseDTO;
import com.quitto.server.domain.enums.IA.AIModelCapability;
import com.quitto.server.domain.enums.IA.AIProviderType;
import com.quitto.server.domain.interfaces.OperationKey.OperationKey;
import com.quitto.server.domain.models.IA.AIModel;
import com.quitto.server.domain.models.IA.AIModelPricing;

/**
 * DTO de resposta para um modelo de IA (catálogo de um provedor).
 *
 * <p>Espelha {@link AIModel} (domínio) para não expor o modelo puro
 * diretamente na camada REST.</p>
 */
public record AIModelDTO(
        OperationKey idempotencyKey,
        String id,
        String name,
        AIProviderType provider,
        Set<AIModelCapability> capabilities,
        AIModelPricing pricing
) implements BaseDTO {

    public static AIModelDTO from(AIModel model) {
        return from(model, null);
    }

    public static AIModelDTO from(AIModel model, OperationKey idempotencyKey) {
        return new AIModelDTO(
                idempotencyKey,
                model.getId(),
                model.getName(),
                model.getProvider(),
                model.getCapabilities(),
                model.getPricing()
        );
    }
}