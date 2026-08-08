package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class NovitaAIProvider extends BaseProvider {

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.NOVITA_AI;
    }

    @Override
    public String getName() {
        return ServiceProvider.NOVITA_AI.name();
    }

    @Override
    public String getApiBaseURL() {
        // Listagem de modelos em modo OpenAI-compat: GET /openai/v1/models
        return "https://api.novita.ai/openai/v1";
    }
}