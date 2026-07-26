package com.quitto.server.infrastructure.Mappers.Cookies;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.quitto.server.domain.valueobject.CookieDomain;
import com.quitto.server.infrastructure.interfaces.Cookies.HttpCookieMapper;

@Service
public class CookieMapper implements HttpCookieMapper{

    @Override
    public ResponseCookie.ResponseCookieBuilder toFrameworkCookie(CookieDomain cookieDomain){
        return ResponseCookie.from(cookieDomain.name(), cookieDomain.value())
                .httpOnly(cookieDomain.httpOnly())
                .secure(cookieDomain.secure())
                .path(cookieDomain.path())
                .sameSite("Lax");
    }
}
