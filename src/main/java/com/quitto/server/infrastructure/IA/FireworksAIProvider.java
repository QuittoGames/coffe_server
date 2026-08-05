package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class FireworksAIProvider extends BaseProvider {

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.FIREWORKS_AI;
    }

    @Override
    public String getName() {
        return ServiceProvider.FIREWORKS_AI.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://api.fireworks.ai/v1";
    }
}