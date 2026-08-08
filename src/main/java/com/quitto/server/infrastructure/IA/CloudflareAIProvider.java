package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;

import org.springframework.stereotype.Service;

/**
 * Cloudflare Workers AI — usa um API Token no header {@code Authorization: Bearer}.
 * A URL base exige o account_id do usuário.
 */
@Service
public class CloudflareAIProvider extends BaseProvider {

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.CLOUDFLARE_AI;
    }

    @Override
    public String getName() {
        return ServiceProvider.CLOUDFLARE_AI.name();
    }

    @Override
    public String getApiBaseURL() {
        return "https://api.cloudflare.com/client/v4/accounts/YOUR_ACCOUNT_ID";
    }

    @Override
    protected String modelsUrl() {
        // GET /ai/models/search?task=Text Generation — o endpoint /ai/v1/models não
        // existe (issue cloudflare/ai#549 → 405). O parser padrão do BaseProvider já
        // cobre a raiz "result" (o id de cada modelo é o valor de "@cf/...").
        return getApiBaseURL() + "/ai/models/search?task=Text%20Generation";
    }
}
