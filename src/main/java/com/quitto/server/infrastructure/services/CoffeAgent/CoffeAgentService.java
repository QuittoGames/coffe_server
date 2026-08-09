package com.quitto.server.infrastructure.services.CoffeAgent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * Serviço para resolver secrets/environment variables dos provedores de IA.
 * <p>
 * Lê chaves de API do ambiente via variáveis de ambiente ou propriedades Spring
 * (ex.: OPENAI_API_KEY, ANTHROPIC_API_KEY, GOOGLE_AI_STUDIO_KEY, etc.).
 * </p>
 */
@Service
public class CoffeAgentService {

    @Value("${api.security.key}")
    private String token;

    public String getEnvKey(String providerName) {
        return "";
    }

    public String getServerToken() throws IllegalArgumentException {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Server token is not configured or is null");
        }
        return token;
    }
}
