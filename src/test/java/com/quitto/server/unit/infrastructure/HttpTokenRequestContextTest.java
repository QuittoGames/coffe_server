package com.quitto.server.unit.infrastructure;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.quitto.server.domain.valueobject.Cookie.CookieDomain;
import com.quitto.server.infrastructure.Adapters.in.HttpTokenRequestContext;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Suite unitária do adapter HttpServletRequest -> TokenRequestContext.
 * (Testes movidos de CookieSystemTest, que duplicava as suites unit/* )
 */
class HttpTokenRequestContextTest {

    @Test
    void getsCookieFromRequest() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getCookies()).thenReturn(new Cookie[]{ new Cookie("access_token", "cookie-value") });

        HttpTokenRequestContext ctx = new HttpTokenRequestContext(req);
        Optional<CookieDomain> result = ctx.getCookie("access_token");

        assertTrue(result.isPresent());
        assertEquals("access_token", result.get().name());
        assertEquals("cookie-value", result.get().value());
    }

    @Test
    void transfersCookieFlagsToDomain() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        Cookie raw = new Cookie("access_token", "cookie-value");
        raw.setHttpOnly(true);
        raw.setSecure(true);
        raw.setPath("/");
        when(req.getCookies()).thenReturn(new Cookie[]{ raw });

        Optional<CookieDomain> result = new HttpTokenRequestContext(req).getCookie("access_token");

        assertTrue(result.isPresent());
        assertTrue(result.get().httpOnly());
        assertTrue(result.get().secure());
        assertEquals("/", result.get().path());
    }

    @Test
    void returnsEmptyWhenCookiesNull() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getCookies()).thenReturn(null);

        HttpTokenRequestContext ctx = new HttpTokenRequestContext(req);

        assertTrue(ctx.getCookie("access_token").isEmpty());
    }

    @Test
    void returnsEmptyWhenCookieNameNotFound() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getCookies()).thenReturn(new Cookie[]{ new Cookie("other", "v") });

        HttpTokenRequestContext ctx = new HttpTokenRequestContext(req);

        assertTrue(ctx.getCookie("access_token").isEmpty());
    }

    @Test
    void getsHeader() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getHeader("Authorization")).thenReturn("Bearer token123");

        HttpTokenRequestContext ctx = new HttpTokenRequestContext(req);
        Optional<String> result = ctx.getHeader("Authorization");

        assertTrue(result.isPresent());
        assertEquals("Bearer token123", result.get());
    }

    @Test
    void returnsEmptyWhenHeaderMissing() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getHeader("Authorization")).thenReturn(null);

        HttpTokenRequestContext ctx = new HttpTokenRequestContext(req);

        assertTrue(ctx.getHeader("Authorization").isEmpty());
    }
}
