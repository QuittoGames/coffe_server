package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class LmStudioProvider extends BaseProvider {

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.LM_STUDIO;
    }

    @Override
    public String getName() {
        return ServiceProvider.LM_STUDIO.name();
    }

    @Override
    public String getApiBaseURL() {
        return "http://localhost:1234/v1";
    }

    @Override
    protected boolean requiresKey() {
        return false;
    }
}