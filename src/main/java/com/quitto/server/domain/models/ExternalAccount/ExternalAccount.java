package com.quitto.server.domain.models.ExternalAccount;

import java.time.LocalDateTime;

import com.quitto.server.domain.enums.Provider;

/**
 * Representa uma conta externa (OAuth2) vinculada a um usuário local.
 *
 * <p>Guarda os tokens de acesso e refresh de um provedor externo
 * ({@link Provider} — ex.: Google, GitHub), além da identidade do usuário
 * no serviço externo ({@code external_client}). O campo {@code expiresAt}
 * indica quando o token de acesso expira, permitindo renovação com o refresh
 * token.</p>
 *
 * <p><strong>Nota:</strong> os campos {@code user_id} e
 * {@code external_client} preservam a grafia snake_case histórica do
 * projeto.</p>
 */
public class ExternalAccount {
    private Long id;
    private Long user_id;
    private Provider provider;
    private String external_client;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime expiresAt;

    public ExternalAccount() {

    }

    public ExternalAccount(Long id, Long user_id, Provider provider, String external_client, String accessToken, String refreshToken, LocalDateTime expiresAt) {
        this.id = id;
        this.user_id = user_id;
        this.provider = provider;
        this.external_client = external_client;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public Provider getProvider() {
        return provider;
    }

    public String getExternal_client() {
        return external_client;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

}
