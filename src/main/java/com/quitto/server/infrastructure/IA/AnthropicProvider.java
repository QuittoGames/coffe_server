package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class AnthropicProvider extends BaseProvider {

    {
        setEnvId("ANTHROPIC");
    }

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.ANTHROPIC;
    }

    @Override
    public String getName() {
        return ServiceProvider.ANTHROPIC.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://api.anthropic.com/v1";
    }

    @Override
    protected java.util.Optional<String> apiKeyHeaderName() {
        return java.util.Optional.of("x-api-key");
    }

    @Override
    protected java.util.Map<String, String> extraHeaders() {
        return java.util.Map.of("anthropic-version", "2023-06-01");
    }
}
