package com.quitto.server.infrastructure.IA;

import org.springframework.stereotype.Service;

import com.quitto.server.domain.enums.IA.AIProviderType;

/**
 * Provedor concreto da OpenAI.
 *
 * <p>Adapter de infraestrutura que herda do {@link BaseProvider} e configura
 * apenas identidade ({@link AIProviderType#OPENAI}) e URL base da API. A
 * listagem real de modelos ({@code fetchModelsFromApi()}) é trabalho futuro.</p>
 */
@Service
public class OpenAIProvider extends BaseProvider {

    public OpenAIProvider() {
        setEnvId("OPENAI");
    }

    @Override
    public AIProviderType getProvider() {
        return AIProviderType.OPENAI;
    }

    @Override
    public String getName() {
        return "OpenAI";
    }

    @Override
    public String getApiBaseURL() {
        return "https://api.openai.com/v1";
    }
}