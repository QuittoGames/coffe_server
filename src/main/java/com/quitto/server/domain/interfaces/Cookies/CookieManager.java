package com.quitto.server.domain.interfaces.Cookies;

import com.quitto.server.domain.valueobject.CookieDomain;

public interface CookieManager {

    CookieDomain createAccessTokenCookie(String value);

    /**
     * Cria o cookie de access token com expiração controlada.
     * Use {@code maxAgeInSeconds = 0} para revogar/limpar o cookie no logout.
     */
    CookieDomain createAccessTokenCookie(String value, Integer maxAgeInSeconds);
}
