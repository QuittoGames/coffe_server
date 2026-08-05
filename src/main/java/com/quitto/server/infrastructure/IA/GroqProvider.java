package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class GroqProvider extends BaseProvider {

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.GROQ;
    }

    @Override
    public String getName() {
        return ServiceProvider.GROQ.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://api.groq.com/openai/v1";
    }
}