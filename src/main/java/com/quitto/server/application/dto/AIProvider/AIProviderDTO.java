package com.quitto.server.application.dto.AIProvider;

import java.util.List;

import com.quitto.server.application.dto.BaseDTO;
import com.quitto.server.domain.enums.IA.AIProviderType;
import com.quitto.server.domain.interfaces.IA.AIProvider;
import com.quitto.server.domain.interfaces.OperationKey.OperationKey;

/**
 * DTO de resposta para um provedor de IA (porta {@link AIProvider} do domínio).
 *
 * <p>Inclui o catálogo de modelos do provedor como {@link AIModelDTO}s e o
 * estado de configuração ({@code configured}) — nunca expõe a chave de API
 * (F1/D4: nenhuma chave cruza a fronteira de aplicação).</p>
 */
public record AIProviderDTO(
        OperationKey idempotencyKey,
        AIProviderType provider,
        String name,
        String apiBaseUrl,
        boolean enabled,
        boolean configured,
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
                provider.isConfigured(),
                provider.getModels().stream()
                        .map(AIModelDTO::from)
                        .toList()
        );
    }
}