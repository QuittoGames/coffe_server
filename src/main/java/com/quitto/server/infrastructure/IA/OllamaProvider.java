package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class OllamaProvider extends BaseProvider {

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.OLLAMA;
    }

    @Override
    public String getName() {
        return ServiceProvider.OLLAMA.name();
    }

    @Override
    public String getApiBaseURL() {
        return "http://localhost:11434";
    }

    @Override
    protected boolean usesBearer() {
        return false;
    }

    @Override
    protected boolean requiresKey() {
        return false;
    }

    @Override
    protected String modelsUrl() {
        // Decisão: endpoint OpenAI-compat GET /v1/models (em vez do nativo /api/tags).
        // Razões: o parser padrão do BaseProvider já cobre a raiz "data"; consistente
        // com vLLM, LM Studio e llama.cpp (mesma família self-hosted); permite listar
        // modelos sem tratamento especial de formato.
        return "http://localhost:11434/v1/models";
    }
}