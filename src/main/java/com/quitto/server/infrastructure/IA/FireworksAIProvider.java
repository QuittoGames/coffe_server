package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

@Service
public class FireworksAIProvider extends BaseProvider {

    private String accountId;

    /**
     * Configura o account_id da conta Fireworks — obrigatório para listar modelos
     * (GET /v1/accounts/{account_id}/models). Deve ser injetado pela aplicação.
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.FIREWORKS_AI;
    }

    @Override
    public String getName() {
        return ServiceProvider.FIREWORKS_AI.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://api.fireworks.ai/v1";
    }

    @Override
    protected String modelsUrl() {
        // GET /v1/accounts/{account_id}/models — o endpoint /v1/models sozinho não existe.
        if (accountId == null || accountId.isBlank()) {
            // TODO: account_id não configurado — fallback para o path genérico mantém o
            // request válido (404 na API, sem crash local). Injete setAccountId(...) para
            // a listagem funcionar.
            return "https://api.fireworks.ai/v1/models";
        }
        return "https://api.fireworks.ai/v1/accounts/" + accountId + "/models";
    }
}