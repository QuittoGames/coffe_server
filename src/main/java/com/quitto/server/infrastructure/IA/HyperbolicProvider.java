package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class HyperbolicProvider extends BaseProvider {

    {
        setEnvId("HYPERBOLIC");
    }

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.HYPERBOLIC;
    }

    @Override
    public String getName() {
        return ServiceProvider.HYPERBOLIC.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://api.hyperbolic.xyz/v1";
    }
}