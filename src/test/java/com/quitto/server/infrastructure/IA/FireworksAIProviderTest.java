package com.quitto.server.infrastructure.IA;

import com.quitto.server.domain.enums.ServiceProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa o {@link FireworksAIProvider} (PG-003): resolução do {@code accountId}
 * a partir do ambiente e a URL de listagem de modelos correspondente.
 *
 * <p>O initializer resolve o {@code accountId} na construção, portanto a
 * propriedade de sistema precisa ser definida ANTES de instanciar.</p>
 */
class FireworksAIProviderTest {

    private static final String ACCOUNT_ID_PROPERTY = "COFFEE_AI_FIREWORKS_ACCOUNT_ID";
    private static final String LEGACY_ACCOUNT_ID_ENV = "FIREWORKS_ACCOUNT_ID";

    @AfterEach
    void cleanUp() {
        System.clearProperty(ACCOUNT_ID_PROPERTY);
    }

    @Test
    void modelsUrl_includesAccountIdWhenConfigured() {
        System.setProperty(ACCOUNT_ID_PROPERTY, "acct_123");

        FireworksAIProvider provider = new FireworksAIProvider();

        assertEquals("https://api.fireworks.ai/v1/accounts/acct_123/models",
                provider.modelsUrl());
    }

    @Test
    void modelsUrl_fallsBackToGenericWhenNoAccountId() {
        FireworksAIProvider provider = new FireworksAIProvider();

        assertEquals("https://api.fireworks.ai/v1/models", provider.modelsUrl());
    }

    @Test
    void modelsUrl_setAccountIdOverridesEnvironment() {
        System.setProperty(ACCOUNT_ID_PROPERTY, "acct_123");

        FireworksAIProvider provider = new FireworksAIProvider();
        provider.setAccountId("acct_456");

        assertEquals("https://api.fireworks.ai/v1/accounts/acct_456/models",
                provider.modelsUrl());
    }

    @Test
    void accountId_isTrimmedWhenResolved() {
        System.setProperty(ACCOUNT_ID_PROPERTY, "  acct_789  ");

        FireworksAIProvider provider = new FireworksAIProvider();

        assertEquals("https://api.fireworks.ai/v1/accounts/acct_789/models",
                provider.modelsUrl());
    }

    @Test
    void identity_isConfigured() {
        FireworksAIProvider provider = new FireworksAIProvider();

        assertEquals(ServiceProvider.FIREWORKS_AI, provider.getProvider());
        assertEquals("FIREWORKS_AI", provider.getName());
        assertEquals("https://api.fireworks.ai/v1", provider.getApiBaseURL());
        assertEquals("FIREWORKS_AI", provider.getEnvId());
        assertTrue(provider.requiresKey());
    }
}