package com.quitto.server.infrastructure.services.CoffeAgent;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CoffeAgentService {

    private final String token;

    public CoffeAgentService(@Value("${api.security.key}") String token) {
        this.token = token;
    }

    /**
     * Chaves de API temporárias (placeholder) por provedor.
     * <p>⚠️ TEMPORÁRIO — PG-001: o fluxo real de secrets (CoffeAgent) ainda não foi
     * implementado. Este hash desbloqueia o catálogo de modelos dos provedores
     * mapeados sem ler variáveis de ambiente; deve ser substituído pela integração
     * real quando o CoffeAgent for implementado.</p>
     */
    private static final Map<String, String> TEMP_PROVIDER_KEYS = Map.of(
            "OPENAI", "sk-placeholder-temp",
            "ANTHROPIC", "sk-placeholder-temp",
            "GOOGLE_AI_STUDIO", "sk-placeholder-temp",
            "GOOGLE_VERTEX_AI", "sk-placeholder-temp",
            "MISTRAL_AI", "sk-placeholder-temp",
            "XAI", "sk-placeholder-temp",
            "COHERE", "sk-placeholder-temp",
            "OPENROUTER", "sk-placeholder-temp",
            "GROQ", "sk-placeholder-temp",
            "TOGETHER_AI", "sk-placeholder-temp");

    /**
     * Resolve a chave de API de um provedor de IA.
     * <p>⚠️ TEMPORÁRIO — PG-001: retorna uma chave placeholder para os provedores
     * mapeados em {@link #TEMP_PROVIDER_KEYS} (não lê o ambiente). Substituir pela
     * integração real de secrets quando o CoffeAgent for implementado.</p>
     * Nunca retorna {@code ""} — ausência é representada por {@code Optional.empty()}
     * (F4: chave vazia não fica persistida no provider).
     *
     * @param providerName nome do provedor (ex.: {@code "OPENAI"})
     * @return a chave placeholder, ou {@code Optional.empty()} se o provedor não está mapeado
     */
    public Optional<String> getEnvKey(String providerName) {
        if (providerName == null || providerName.isBlank()) {
            return Optional.empty();
        }
        String name = providerName.trim().toUpperCase();
        // TODO: substituir pela integração real de secrets (CoffeAgent) quando implementada
        return Optional.ofNullable(TEMP_PROVIDER_KEYS.get(name));
    }

    /**
     * Token interno do servidor usado pelo fluxo WebSocket/MCP
     * (antes chamado {@code getServerToken()}). Ausência → {@code Optional.empty()}.
     */
    public Optional<String> getServerToken() {
        return Optional.ofNullable(token).filter(value -> !value.isBlank());
    }
}
