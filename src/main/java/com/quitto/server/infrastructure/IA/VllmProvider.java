package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class VllmProvider extends BaseProvider {

    {
        setEnvId("VLLM");
    }

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.VLLM;
    }

    @Override
    public String getName() {
        return ServiceProvider.VLLM.name();
    }

    @Override
    public String getApiBaseURL() {
        return "http://localhost:8000/v1";
    }

    @Override
    public boolean requiresKey() {
        return false;
    }
}