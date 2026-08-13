package com.quitto.server.application.dto.AIProvider;

import java.util.List;

import com.quitto.server.application.dto.BaseDTO;
import com.quitto.server.domain.enums.ServiceProvider;
import com.quitto.server.domain.interfaces.IA.AIProvider;
import com.quitto.server.domain.interfaces.OperationKey.OperationKey;

/**
 * DTO de resposta para um provedor de IA (porta {@link AIProvider} do domínio).
 *
 * <p>Inclui o catálogo de modelos do provedor como {@link AIModelDTO}s.</p>
 */
public record AIProviderDTO(
        OperationKey idempotencyKey,
        ServiceProvider provider,
        String name,
        String apiBaseUrl,
        boolean enabled,
        List<AIModelDTO> models
) implements BaseDTO {

    public static AIProviderDTO from(AIProvider provider) {
        return from(provider, null);
    }

    public static AIProviderDTO from(AIProvider provider, OperationKey idempotencyKey) {
        return new AIProviderDTO(
                idempotencyKey,
                provider.getProvider(),
                provider.getName(),
                provider.getApiBaseURL(),
                provider.isEnabled(),
                provider.getModels().stream()
                        .map(AIModelDTO::from)
                        .toList()
        );
    }
}