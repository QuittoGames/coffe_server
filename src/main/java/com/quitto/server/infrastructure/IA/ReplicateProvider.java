package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class ReplicateProvider extends BaseProvider {

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.REPLICATE;
    }

    @Override
    public String getName() {
        return ServiceProvider.REPLICATE.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://api.replicate.com/v1";
    }
}
