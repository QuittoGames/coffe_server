package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class LlamacppProvider extends BaseProvider {

    {
        setEnvId("LLAMACPP");
    }

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.LLAMACPP;
    }

    @Override
    public String getName() {
        return ServiceProvider.LLAMACPP.name();
    }

    @Override
    public String getApiBaseURL() {
        return "http://localhost:8080/v1";
    }

    @Override
    public boolean requiresKey() {
        return false;
    }
}