package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

/**
 * Google Vertex AI — requer um token OAuth2 de acesso como Bearer e o nome da
 * localização (região) na URL base. O token é obtido fora desta classe (ex.:
 * via ADC / service account) e injetado por {@link #setKey(String)}.
 */
@Service
public class GoogleVertexAIProvider extends BaseProvider {

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.GOOGLE_VERTEX_AI;
    }

    @Override
    public String getName() {
        return ServiceProvider.GOOGLE_VERTEX_AI.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://LOCATION-aiplatform.googleapis.com/v1";
    }
}
