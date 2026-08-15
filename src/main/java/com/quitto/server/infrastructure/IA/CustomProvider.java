package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

/**
 * Provedor customizado (OpenAI-compatible). A URL base deve ser configurada
 * antes de usar — troque o placeholder por um endpoint real.
 */
@Service
public class CustomProvider extends BaseProvider {

    {
        setEnvId("CUSTOM");
    }

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.CUSTOM;
    }

    @Override
    public String getName() {
        return ServiceProvider.CUSTOM.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://YOUR_BASE_URL";
    }
}
