package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class CerebrasProvider extends BaseProvider {

    {
        setEnvId("CEREBRAS");
    }

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.CEREBRAS;
    }

    @Override
    public String getName() {
        return ServiceProvider.CEREBRAS.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://api.cerebras.ai/v1";
    }
}
