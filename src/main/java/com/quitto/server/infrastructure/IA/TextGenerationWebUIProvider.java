package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class TextGenerationWebUIProvider extends BaseProvider {

    {
        setEnvId("TEXT_GENERATION_WEBUI");
    }

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.TEXT_GENERATION_WEBUI;
    }

    @Override
    public String getName() {
        return ServiceProvider.TEXT_GENERATION_WEBUI.name();
    }
    @Override
    public String getApiBaseURL() {
        return "http://localhost:5000/v1";
    }

    @Override
    public boolean requiresKey() {
        return false;
    }
}
