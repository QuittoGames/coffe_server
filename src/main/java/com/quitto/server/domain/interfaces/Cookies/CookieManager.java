package com.quitto.server.domain.interfaces.Cookies;

import com.quitto.server.domain.valueobject.CookieDomain;

/**
 * Porta de gerenciamento do cookie de access token (domínio puro).
 *
 * <p>Especializa a criação do cookie de autenticação usado pelo servidor
 * ({@code access_token}), com defaults seguros (HttpOnly, Secure, path=/).
 * A implementação concreta vive na infraestrutura.</p>
 *
 * <p><strong>Por quê existe:</strong> o cookie de autenticação é um conceito
 * de negócio (nome, segurança e expiração padronizados); o domínio define o
 * contrato e a infraestrutura converte para o cookie do framework.</p>
 */
public interface CookieManager {

    CookieDomain createAccessTokenCookie(String value);

    CookieDomain createAccessTokenCookie(String value, Integer maxAgeInSeconds);
}
