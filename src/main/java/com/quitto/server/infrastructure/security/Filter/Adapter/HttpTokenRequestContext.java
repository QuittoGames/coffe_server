package com.quitto.server.infrastructure.security.Filter.Adapter;

import com.quitto.server.domain.interfaces.Token.TokenRequestContext;
import com.quitto.server.domain.valueobject.CookieDomain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

public class HttpTokenRequestContext implements TokenRequestContext {

    private final HttpServletRequest request;

    public HttpTokenRequestContext(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public Optional<String> getHeader(String name) {
        return Optional.ofNullable(request.getHeader(name));
    }

    @Override
    public Optional<CookieDomain> getCookie(String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return Optional.of(new CookieDomain(
                    cookie.getName(),
                    cookie.getValue(),
                    cookie.isHttpOnly(),
                    cookie.getSecure(),
                    cookie.getPath(),
                    cookie.getMaxAge()
                ));
            }
        }
        return Optional.empty();
    }
}
