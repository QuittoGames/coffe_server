package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

/**
 * Provedor genérico compatível com a API OpenAI. A URL base real deve ser
 * configurada quando este provedor for usado (ex.: proxy/endpoint próprio).
 */
@Service
public class OpenAICompatibleProvider extends BaseProvider {

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.OPENAI_COMPATIBLE;
    }

    @Override
    public String getName() {
        return ServiceProvider.OPENAI_COMPATIBLE.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://api.openai.com/v1";
    }
}