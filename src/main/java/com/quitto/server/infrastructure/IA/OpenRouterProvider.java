package com.quitto.server.infrastructure.IA;

import org.springframework.stereotype.Service;

import com.quitto.server.domain.enums.IA.AIProviderType;

/**
 * Provedor concreto da OpenRouter.
 *
 * <p>Adapter de infraestrutura que herda do {@link BaseProvider} e configura
 * apenas identidade ({@link AIProviderType#OPENROUTER}) e URL base da API. A
 * listagem real de modelos ({@code fetchModelsFromApi()}) é trabalho futuro.</p>
 */
@Service
public class OpenRouterProvider extends BaseProvider {

    public OpenRouterProvider() {
        setEnvId("OPENROUTER");
    }

    @Override
    public AIProviderType getProvider() {
        return AIProviderType.OPENROUTER;
    }

    @Override
    public String getName() {
        return "OpenRouter";
    }

    @Override
    public String getApiBaseURL() {
        return "https://openrouter.ai/api/v1";
    }
}