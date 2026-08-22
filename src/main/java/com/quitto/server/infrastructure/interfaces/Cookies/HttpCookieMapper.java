package com.quitto.server.infrastructure.interfaces.Cookies;

import org.springframework.http.ResponseCookie;

import com.quitto.server.domain.valueobject.Cookie.CookieDomain;

public interface HttpCookieMapper {
    ResponseCookie.ResponseCookieBuilder toFrameworkCookie(CookieDomain cookieDomain);
}