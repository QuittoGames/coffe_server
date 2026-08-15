package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class PerplexityProvider extends BaseProvider {

    {
        setEnvId("PERPLEXITY");
    }

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.PERPLEXITY;
    }

    @Override
    public String getName() {
        return ServiceProvider.PERPLEXITY.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://api.perplexity.ai/v1";
    }
}
