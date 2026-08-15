package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class OpenRouterProvider extends BaseProvider {

    {
        setEnvId("OPENROUTER");
    }

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.OPENROUTER;
    }

    @Override
    public String getName() {
        return ServiceProvider.OPENROUTER.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://openrouter.ai/api/v1";
    }
}