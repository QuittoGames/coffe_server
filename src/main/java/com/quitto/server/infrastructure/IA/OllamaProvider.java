package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class OllamaProvider extends BaseProvider {

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.OLLAMA;
    }

    @Override
    public String getName() {
        return ServiceProvider.OLLAMA.name();
    }

    @Override
    public String getApiBaseURL() {
        return "http://localhost:11434";
    }

    @Override
    protected boolean usesBearer() {
        return false;
    }

    @Override
    protected boolean requiresKey() {
        return false;
    }
}