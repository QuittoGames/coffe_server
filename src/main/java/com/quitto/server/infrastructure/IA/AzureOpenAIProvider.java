package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class AzureOpenAIProvider extends BaseProvider {

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.AZURE_OPENAI;
    }

    @Override
    public String getName() {
        return ServiceProvider.AZURE_OPENAI.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://YOUR-RESOURCE.openai.azure.com/openai";
    }

    @Override
    protected java.util.Optional<String> apiKeyHeaderName() {
        return java.util.Optional.of("api-key");
    }

    @Override
    protected String modelsUrl() {
        // GET /openai/models?api-version=... — o Azure OpenAI exige o parâmetro
        // obrigatório api-version em toda chamada de API.
        return getApiBaseURL() + "/models?api-version=2024-10-21";
    }
}