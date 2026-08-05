package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class NvidiaProvider extends BaseProvider {

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.NVIDIA;
    }

    @Override
    public String getName() {
        return ServiceProvider.NVIDIA.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://integrate.api.nvidia.com/v1";
    }
}