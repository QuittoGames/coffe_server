package com.quitto.server.infrastructure.Adpter.out;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.quitto.server.domain.valueobject.CookieDomain;
import com.quitto.server.infrastructure.interfaces.Cookies.HttpCookieMapper;
import com.quitto.server.infrastructure.interfaces.Cookies.HttpCookieWriter;
import com.quitto.server.infrastructure.services.Auth.Token.Cookies.HttpCookieWriterManeger;
import com.quitto.server.domain.interfaces.Cookies.CookieManager;
import com.quitto.server.domain.interfaces.Cookies.CookieFactory;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieManagerAdapter implements CookieManager {

    private final CookieFactory cookieFactory;
    private final HttpCookieWriter cookieWriter;

    public CookieManagerAdapter(CookieFactory cookieService, HttpCookieWriter cookieWriter) {
        this.cookieFactory = cookieService;
        this.cookieWriter = cookieWriter;
    }

    @Override
    public CookieDomain createAccessTokenCookie(String value) {
        return cookieFactory.createCookie("access_token", value);
    }

    public void writeCookie(HttpServletResponse response, CookieDomain cookieDomain) {
        Objects.requireNonNull(response, "response cannot be null");
        Objects.requireNonNull(cookieDomain, "cookieDomain cannot be null");

    }
}
