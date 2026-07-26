package com.quitto.server.infrastructure.interfaces.Cookies;

import com.quitto.server.domain.valueobject.CookieDomain;

import jakarta.servlet.http.HttpServletResponse;

public interface HttpCookieWriter {
    void writeCookie(HttpServletResponse response, CookieDomain cookieDomain);
}
