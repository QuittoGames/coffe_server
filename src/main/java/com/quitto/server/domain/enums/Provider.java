package com.quitto.server.domain.enums;

/**
 * Provedores de autenticação externa (OAuth2) suportados pelo sistema.
 * Usado pelo modelo {@code ExternalAccount} para identificar a origem da
 * conta externa vinculada ao usuário local.
 */
public enum Provider {
    GOOGLE,
    GITHUB
}
