package com.quitto.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.quitto.server.domain.interfaces.Token.TokenRequestContext;
import com.quitto.server.domain.interfaces.Token.TokenResolver;
import com.quitto.server.domain.valueobject.CookieDomain;
import com.quitto.server.infrastructure.security.Filter.Adapter.HttpTokenRequestContext;
import com.quitto.server.infrastructure.security.Filter.Token.CookieTokenResolver;
import com.quitto.server.infrastructure.security.Filter.Token.JwtTokenResolver;
import com.quitto.server.infrastructure.services.Auth.Token.Cookies.HttpCookieService;
import com.quitto.server.infrastructure.services.Auth.Token.TokenResolverManager;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

class CookieSystemTest {

    // --- CookieDomain ---

    @Test
    void cookieDomain_createsWithDefaults() {
        CookieDomain cookie = CookieDomain.of("token", "abc123");

        assertEquals("token", cookie.name());
        assertEquals("abc123", cookie.value());
        assertTrue(cookie.httpOnly());
        assertTrue(cookie.secure());
        assertEquals("/", cookie.path());
        assertNull(cookie.maxAge());
    }

    @Test
    void cookieDomain_createsWithPathAndMaxAge() {
        CookieDomain cookie = CookieDomain.of("token", "abc123", "/api", 3600);

        assertEquals("/api", cookie.path());
        assertEquals(3600, cookie.maxAge());
    }

    @Test
    void cookieDomain_rejectsNullName() {
        assertThrows(NullPointerException.class, () -> new CookieDomain(null, "value", true, true, "/", null));
    }

    @Test
    void cookieDomain_rejectsNullValue() {
        assertThrows(NullPointerException.class, () -> new CookieDomain("name", null, true, true, "/", null));
    }

    @Test
    void cookieDomain_rejectsInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> CookieDomain.of("bad=name", "value"));
        assertThrows(IllegalArgumentException.class, () -> CookieDomain.of("bad;name", "value"));
    }

    // --- JwtTokenResolver ---

    @Test
    void jwtResolver_extractsBearerToken() {
        TokenRequestContext ctx = mock(TokenRequestContext.class);
        when(ctx.getHeader("Authorization")).thenReturn(Optional.of("Bearer my-jwt-token"));

        JwtTokenResolver resolver = new JwtTokenResolver();
        Optional<String> result = resolver.resolve(ctx);

        assertTrue(result.isPresent());
        assertEquals("my-jwt-token", result.get());
    }

    @Test
    void jwtResolver_returnsEmptyWhenNoAuthHeader() {
        TokenRequestContext ctx = mock(TokenRequestContext.class);
        when(ctx.getHeader("Authorization")).thenReturn(Optional.empty());

        JwtTokenResolver resolver = new JwtTokenResolver();
        Optional<String> result = resolver.resolve(ctx);

        assertTrue(result.isEmpty());
    }

    @Test
    void jwtResolver_returnsEmptyWhenBlankHeader() {
        TokenRequestContext ctx = mock(TokenRequestContext.class);
        when(ctx.getHeader("Authorization")).thenReturn(Optional.of(""));

        JwtTokenResolver resolver = new JwtTokenResolver();
        Optional<String> result = resolver.resolve(ctx);

        assertTrue(result.isEmpty());
    }

    // --- CookieTokenResolver ---

    @Test
    void cookieResolver_extractsAccessToken() {
        TokenRequestContext ctx = mock(TokenRequestContext.class);
        CookieDomain cookie = CookieDomain.of("access_token", "my-jwt-token");
        when(ctx.getCookie("access_token")).thenReturn(Optional.of(cookie));

        CookieTokenResolver resolver = new CookieTokenResolver();
        Optional<String> result = resolver.resolve(ctx);

        assertTrue(result.isPresent());
        assertEquals("my-jwt-token", result.get());
    }

    @Test
    void cookieResolver_returnsEmptyWhenNoCookie() {
        TokenRequestContext ctx = mock(TokenRequestContext.class);
        when(ctx.getCookie("access_token")).thenReturn(Optional.empty());

        CookieTokenResolver resolver = new CookieTokenResolver();
        Optional<String> result = resolver.resolve(ctx);

        assertTrue(result.isEmpty());
    }

    // --- TokenResolverManager ---

    @Test
    void manager_usesFirstMatchingResolver() {
        TokenResolver first = mock(TokenResolver.class);
        TokenResolver second = mock(TokenResolver.class);
        when(first.resolve(any())).thenReturn(Optional.of("from-first"));
        when(second.resolve(any())).thenReturn(Optional.of("from-second"));

        TokenResolverManager manager = new TokenResolverManager(List.of(first, second));
        Optional<String> result = manager.resolve(mock(TokenRequestContext.class));

        assertEquals("from-first", result.get());
        verify(second, never()).resolve(any());
    }

    @Test
    void manager_fallsBackToNextResolverWhenFirstEmpty() {
        TokenResolver first = mock(TokenResolver.class);
        TokenResolver second = mock(TokenResolver.class);
        when(first.resolve(any())).thenReturn(Optional.empty());
        when(second.resolve(any())).thenReturn(Optional.of("from-second"));

        TokenResolverManager manager = new TokenResolverManager(List.of(first, second));
        Optional<String> result = manager.resolve(mock(TokenRequestContext.class));

        assertEquals("from-second", result.get());
    }

    @Test
    void manager_returnsEmptyWhenNoResolverMatches() {
        TokenResolver first = mock(TokenResolver.class);
        TokenResolver second = mock(TokenResolver.class);
        when(first.resolve(any())).thenReturn(Optional.empty());
        when(second.resolve(any())).thenReturn(Optional.empty());

        TokenResolverManager manager = new TokenResolverManager(List.of(first, second));
        Optional<String> result = manager.resolve(mock(TokenRequestContext.class));

        assertTrue(result.isEmpty());
    }

    // --- HttpTokenRequestContext (integration point) ---

    @Test
    void httpTokenContext_getsCookieFromRequest() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        Cookie cookie = new Cookie("access_token", "cookie-value");
        when(req.getCookies()).thenReturn(new Cookie[]{cookie});

        HttpTokenRequestContext ctx = new HttpTokenRequestContext(req);
        Optional<CookieDomain> result = ctx.getCookie("access_token");

        assertTrue(result.isPresent());
        assertEquals("cookie-value", result.get().value());
    }

    @Test
    void httpTokenContext_returnsEmptyWhenCookiesNull() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getCookies()).thenReturn(null);

        HttpTokenRequestContext ctx = new HttpTokenRequestContext(req);
        Optional<CookieDomain> result = ctx.getCookie("access_token");

        assertTrue(result.isEmpty());
    }

    @Test
    void httpTokenContext_getsHeader() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getHeader("Authorization")).thenReturn("Bearer token123");

        HttpTokenRequestContext ctx = new HttpTokenRequestContext(req);
        Optional<String> result = ctx.getHeader("Authorization");

        assertTrue(result.isPresent());
        assertEquals("Bearer token123", result.get());
    }

    // --- HttpCookieService ---

    @Test
    void cookieService_createsCookie() {
        HttpCookieService service = new HttpCookieService(false);
        CookieDomain cookie = service.createCookie("x", "y");

        assertEquals("x", cookie.name());
        assertEquals("y", cookie.value());
    }

    @Test
    void cookieService_createsCookieWithPathAndMaxAge() {
        HttpCookieService service = new HttpCookieService(false);
        CookieDomain cookie = service.createCookie("x", "y", "/path", 1800);

        assertEquals("/path", cookie.path());
        assertEquals(1800, cookie.maxAge());
    }
}
