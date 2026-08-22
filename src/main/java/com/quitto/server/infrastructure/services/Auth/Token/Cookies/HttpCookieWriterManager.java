package com.quitto.server.infrastructure.services.Auth.Token.Cookies;

import org.springframework.stereotype.Service;
import org.springframework.http.ResponseCookie;

import com.quitto.server.domain.interfaces.Cookies.CookieService;
import com.quitto.server.domain.valueobject.Cookie.CookieDomain;

import jakarta.servlet.http.HttpServletResponse;

import java.util.Objects;

/**
 * Implementação concreta de {@link HttpCookieWriter} para Jakarta Servlet.
 * 
 * <p>Esta classe converte {@link CookieDomain} do domínio para
 * {@link jakarta.servlet.http.Cookie} usando o {@link CookieService} para
 * a conversão básica e aplicando configurações específicas do servlet.</p>
 */
@Service
public class HttpCookieWriterManager implements HttpCookieWriter {

    private final CookieService cookieService;

    public HttpCookieWriterManager(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    @Override
    public void writeCookie(HttpServletResponse response, CookieDomain cookieDomain) {
        Objects.requireNonNull(response, "response cannot be null");
        Objects.requireNonNull(cookieDomain, "cookieDomain cannot be null");

        // Use the domain service to get the framework-specific representation
        ResponseCookie.ResponseCookieBuilder builder = 
            ((ResponseCookie.ResponseCookieBuilder) cookieService.toFrameworkCookie(cookieDomain));

        if (cookieDomain.maxAge() != null) {
            builder.maxAge(cookieDomain.maxAge());
        }

        response.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, builder.build().toString());
    }
}