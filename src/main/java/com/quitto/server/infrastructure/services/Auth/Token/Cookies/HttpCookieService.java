package com.quitto.server.infrastructure.services.Auth.Token.Cookies;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.quitto.server.domain.valueobject.CookieDomain;
import com.quitto.server.domain.interfaces.Cookies.CookieFactory;

@Service
public class HttpCookieService implements CookieFactory {

    private final boolean secure;

    public HttpCookieService(@Value("${app.cookie.secure:false}") boolean secure) {
        this.secure = secure;
    }

    @Override
    public CookieDomain createCookie(String name, String value) {
        return new CookieDomain(name, value, true, this.secure, "/", null);
    }

    @Override
    public CookieDomain createCookie(String name, String value, String path, Integer maxAgeInSeconds) {
        return new CookieDomain(name, value, true, this.secure, path, maxAgeInSeconds);
    }
}
