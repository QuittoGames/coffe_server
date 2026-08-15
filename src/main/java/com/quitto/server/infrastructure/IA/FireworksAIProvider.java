package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FireworksAIProvider extends BaseProvider {

    private static final Logger log = LoggerFactory.getLogger(FireworksAIProvider.class);

    private String accountId;

    {
        setEnvId("FIREWORKS_AI");
        this.accountId = resolveAccountId();
    }

    /**
     * Configura o account_id da conta Fireworks — obrigatório para listar modelos
     * (GET /v1/accounts/{account_id}/models). Deve ser injetado pela aplicação.
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * Resolve o account_id do ambiente: {@code COFFEE_AI_FIREWORKS_ACCOUNT_ID}
     * (env var ou system property) com fallback legado {@code FIREWORKS_ACCOUNT_ID}.
     * Pode ser sobrescrito via {@link #setAccountId(String)}.
     */
    private static String resolveAccountId() {
        String value = System.getenv("COFFEE_AI_FIREWORKS_ACCOUNT_ID");
        if (value == null || value.isBlank()) {
            value = System.getProperty("COFFEE_AI_FIREWORKS_ACCOUNT_ID");
        }
        if (value == null || value.isBlank()) {
            value = System.getenv("FIREWORKS_ACCOUNT_ID");
        }
        return (value == null || value.isBlank()) ? null : value.trim();
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
            log.warn("account_id não configurado para Fireworks (env: COFFEE_AI_FIREWORKS_ACCOUNT_ID) — "
                    + "fallback para /v1/models; a listagem retornará 404. Injete setAccountId(...) para a listagem funcionar.");
            return "https://api.fireworks.ai/v1/models";
        }
        return "https://api.fireworks.ai/v1/accounts/" + accountId + "/models";
    }
}