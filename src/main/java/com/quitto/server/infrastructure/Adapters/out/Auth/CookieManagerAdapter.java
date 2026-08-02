package com.quitto.server.infrastructure.Adapters.out.Auth;

import org.springframework.stereotype.Component;

import com.quitto.server.domain.valueobject.CookieDomain;
import com.quitto.server.domain.interfaces.Cookies.CookieManager;
import com.quitto.server.domain.interfaces.Cookies.CookieFactory;

@Component
public class CookieManagerAdapter implements CookieManager {

    private final CookieFactory cookieFactory;

    public CookieManagerAdapter(CookieFactory cookieService) {
        this.cookieFactory = cookieService;
    }

    @Override
    public CookieDomain createAccessTokenCookie(String value) {
        return cookieFactory.createCookie("access_token", value);
    }

    @Override
    public CookieDomain createAccessTokenCookie(String value, Integer maxAgeInSeconds) {
        return cookieFactory.createCookie("access_token", value, "/", maxAgeInSeconds);
    }
}
