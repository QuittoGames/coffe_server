package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class TogetherAIProvider extends BaseProvider {

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.TOGETHER_AI;
    }

    @Override
    public String getName() {
        return ServiceProvider.TOGETHER_AI.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://api.together.ai/v1";
    }
}