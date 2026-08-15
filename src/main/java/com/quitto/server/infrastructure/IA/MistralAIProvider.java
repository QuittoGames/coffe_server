package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class MistralAIProvider extends BaseProvider {

    {
        setEnvId("MISTRAL_AI");
    }

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.MISTRAL_AI;
    }

    @Override
    public String getName() {
        return ServiceProvider.MISTRAL_AI.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://api.mistral.ai/v1";
    }
}