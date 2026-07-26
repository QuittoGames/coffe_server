package com.quitto.server.domain.interfaces.Cookies;

import com.quitto.server.domain.valueobject.CookieDomain;

public interface CookieManager {

    CookieDomain createAccessTokenCookie(String value);
}
