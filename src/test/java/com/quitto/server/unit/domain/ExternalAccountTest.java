package com.quitto.server.unit.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.quitto.server.domain.enums.Provider;
import com.quitto.server.domain.models.ExternalAccount.ExternalAccount;

@DisplayName("ExternalAccount Domain Tests")
class ExternalAccountTest {

    @Test
    @DisplayName("creates ExternalAccount with full constructor")
    void createsExternalAccountWithFullConstructor() {
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);
        ExternalAccount account = new ExternalAccount(
            1L, 42L, Provider.GOOGLE, "client-123",
            "access-token-abc", "refresh-token-xyz", expiresAt
        );

        assertEquals(1L, account.getId());
        assertEquals(42L, account.getUser_id());
        assertEquals(Provider.GOOGLE, account.getProvider());
        assertEquals("client-123", account.getExternal_client());
        assertEquals("access-token-abc", account.getAccessToken());
        assertEquals("refresh-token-xyz", account.getRefreshToken());
        assertEquals(expiresAt, account.getExpiresAt());
    }

    @Test
    @DisplayName("creates ExternalAccount with no-args")
    void createsExternalAccountWithNoArgsConstructor() {
        ExternalAccount account = new ExternalAccount();
        assertNotNull(account);
        assertNull(account.getId());
        assertNull(account.getProvider());
        assertNull(account.getAccessToken());
    }

    @Test
    @DisplayName("setRefreshToken updates token")
    void setRefreshToken_updatesToken() {
        ExternalAccount account = new ExternalAccount();
        account.setRefreshToken("new-refresh-token");
        assertEquals("new-refresh-token", account.getRefreshToken());
        account.setRefreshToken(null);
        assertNull(account.getRefreshToken());
    }

    @Test
    @DisplayName("setExpiresAt updates expiration")
    void setExpiresAt_updatesExpiration() {
        ExternalAccount account = new ExternalAccount();
        LocalDateTime future = LocalDateTime.now().plusDays(30);
        account.setExpiresAt(future);
        assertEquals(future, account.getExpiresAt());
    }

    @Test
    @DisplayName("handles GOOGLE and GITHUB providers")
    void handlesBothProviders() {
        ExternalAccount google = new ExternalAccount(
            1L, 1L, Provider.GOOGLE, "c1", "a1", "r1", null);
        ExternalAccount github = new ExternalAccount(
            2L, 2L, Provider.GITHUB, "c2", "a2", "r2", null);

        assertEquals(Provider.GOOGLE, google.getProvider());
        assertEquals(Provider.GITHUB, github.getProvider());
        assertEquals(2, Provider.values().length);
    }

    @Test
    @DisplayName("expiresAt handles past and future dates")
    void expiresAt_handlesDates() {
        ExternalAccount account = new ExternalAccount();

        LocalDateTime past = LocalDateTime.now().minusDays(1);
        account.setExpiresAt(past);
        assertTrue(account.getExpiresAt().isBefore(LocalDateTime.now()));

        LocalDateTime future = LocalDateTime.now().plusDays(1);
        account.setExpiresAt(future);
        assertTrue(account.getExpiresAt().isAfter(LocalDateTime.now()));
    }
}
