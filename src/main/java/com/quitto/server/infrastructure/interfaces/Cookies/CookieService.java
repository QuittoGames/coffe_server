package com.quitto.server.infrastructure.interfaces;

import com.quitto.server.domain.valueobject.CookieDomain;

import jakarta.servlet.http.HttpServletResponse;

public interface CookieService {
    CookieDomain createCookie(String name, String value);

    CookieDomain createCookie(
        String name,
        String value,
        String path,
        Integer maxAgeInSeconds
    );

    void writeCookie(HttpServletResponse response, CookieDomain cookie);
}
