package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class GoogleAIStudioProvider extends BaseProvider {

    {
        setEnvId("GOOGLE_AI_STUDIO");
    }

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.GOOGLE_AI_STUDIO;
    }

    @Override
    public String getName() {
        return ServiceProvider.GOOGLE_AI_STUDIO.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://generativelanguage.googleapis.com/v1beta";
    }

    @Override
    protected boolean usesBearer() {
        return false;
    }

    @Override
    protected Optional<String> apiKeyQueryParamName() {
        return Optional.of("key");
    }
}