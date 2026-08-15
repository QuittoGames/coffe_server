package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class SambaNovaProvider extends BaseProvider {

    {
        setEnvId("SAMBANOVA");
    }

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.SAMBANOVA;
    }

    @Override
    public String getName() {
        return ServiceProvider.SAMBANOVA.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://api.sambanova.ai/v1";
    }
}