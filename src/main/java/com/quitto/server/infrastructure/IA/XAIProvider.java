package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class XAIProvider extends BaseProvider {

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.XAI;
    }

    @Override
    public String getName() {
        return ServiceProvider.XAI.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://api.x.ai/v1";
    }
}