package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class CohereProvider extends BaseProvider {

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.COHERE;
    }

    @Override
    public String getName() {
        return ServiceProvider.COHERE.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://api.cohere.com";
    }
}