package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class OpenAIProvider extends BaseProvider {

    {
        setEnvId("OPENAI");
    }

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.OPENAI;
    }

    @Override
    public String getName() {
        return ServiceProvider.OPENAI.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://api.openai.com/v1";
    }
}