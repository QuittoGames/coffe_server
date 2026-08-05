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
}
