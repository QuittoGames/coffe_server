package com.quitto.server.unit.infrastructure.services.CoffeAgent;

import com.quitto.server.infrastructure.services.CoffeAgent.CoffeAgentService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa {@link CoffeAgentService#getEnvKey(String)} e {@link CoffeAgentService#getServerToken()}
 * (PG-001/PG-005).
 *
 * <p>O {@code getEnvKey} usa um hash TEMPORÁRIO de chaves placeholder (PG-001) —
 * os testes validam o contrato: provedor mapeado → presente; não mapeado → vazio;
 * nunca retorna {@code ""}; entrada null/blank → vazio.</p>
 */
class CoffeAgentServiceTest {

    // ── getEnvKey: entradas inválidas ───────────────────────────────────────

    @Test
    void getEnvKey_null_returnsEmpty() {
        CoffeAgentService service = new CoffeAgentService("token");

        assertEquals(Optional.empty(), service.getEnvKey(null));
    }

    @Test
    void getEnvKey_blank_returnsEmpty() {
        CoffeAgentService service = new CoffeAgentService("token");

        assertEquals(Optional.empty(), service.getEnvKey(""));
        assertEquals(Optional.empty(), service.getEnvKey("   "));
    }

    // ── getEnvKey: hash temporário de placeholders ──────────────────────────

    @Test
    void getEnvKey_mappedProvider_returnsPlaceholder() {
        CoffeAgentService service = new CoffeAgentService("token");

        Optional<String> key = service.getEnvKey("OPENAI");

        assertTrue(key.isPresent());
        assertFalse(key.get().isBlank());
    }

    @Test
    void getEnvKey_normalizesProviderName() {
        CoffeAgentService service = new CoffeAgentService("token");

        assertEquals(Optional.of("sk-placeholder-temp"), service.getEnvKey("openai"));
        assertEquals(Optional.of("sk-placeholder-temp"), service.getEnvKey("  OPENAI  "));
    }

    @Test
    void getEnvKey_unmappedProvider_returnsEmpty() {
        CoffeAgentService service = new CoffeAgentService("token");

        assertEquals(Optional.empty(), service.getEnvKey("ZZZTEST"));
    }

    @Test
    void getEnvKey_neverReturnsEmptyString() {
        CoffeAgentService service = new CoffeAgentService("token");

        for (String provider : new String[]{"OPENAI", "ANTHROPIC", "GOOGLE_AI_STUDIO",
                "GOOGLE_VERTEX_AI", "MISTRAL_AI", "XAI", "COHERE", "OPENROUTER",
                "GROQ", "TOGETHER_AI"}) {
            Optional<String> key = service.getEnvKey(provider);
            assertTrue(key.isPresent());
            assertNotEquals("", key.get());
        }
    }

    // ── getServerToken ──────────────────────────────────────────────────────

    @Test
    void getServerToken_returnsTokenWhenPresent() {
        CoffeAgentService service = new CoffeAgentService("server-token");

        assertEquals(Optional.of("server-token"), service.getServerToken());
    }

    @Test
    void getServerToken_returnsEmptyWhenNull() {
        CoffeAgentService service = new CoffeAgentService(null);

        assertEquals(Optional.empty(), service.getServerToken());
    }

    @Test
    void getServerToken_returnsEmptyWhenBlank() {
        CoffeAgentService service = new CoffeAgentService("   ");

        assertEquals(Optional.empty(), service.getServerToken());
    }
}