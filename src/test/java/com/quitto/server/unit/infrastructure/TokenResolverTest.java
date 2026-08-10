package com.quitto.server.unit.infrastructure;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.quitto.server.domain.interfaces.Token.TokenRequestContext;
import com.quitto.server.domain.valueobject.Cookie.CookieDomain;
import com.quitto.server.infrastructure.security.Token.CookieTokenResolver;
import com.quitto.server.infrastructure.security.Token.JwtTokenResolver;

class TokenResolverTest {

    private final JwtTokenResolver jwtResolver = new JwtTokenResolver();
    private final CookieTokenResolver cookieResolver = new CookieTokenResolver();

    // --- JwtTokenResolver ---

    @Test
    void jwt_extractsBearerToken() {
        TokenRequestContext ctx = mock(TokenRequestContext.class);
        when(ctx.getHeader("Authorization")).thenReturn(Optional.of("Bearer my-jwt-token"));

        Optional<String> result = jwtResolver.resolve(ctx);
        assertTrue(result.isPresent());
        assertEquals("my-jwt-token", result.get());
    }

    @Test
    void jwt_returnsEmptyWhenNoAuthHeader() {
        TokenRequestContext ctx = mock(TokenRequestContext.class);
        when(ctx.getHeader("Authorization")).thenReturn(Optional.empty());

        assertTrue(jwtResolver.resolve(ctx).isEmpty());
    }

    @Test
    void jwt_returnsEmptyWhenBlankHeader() {
        TokenRequestContext ctx = mock(TokenRequestContext.class);
        when(ctx.getHeader("Authorization")).thenReturn(Optional.of(""));

        assertTrue(jwtResolver.resolve(ctx).isEmpty());
    }

    @Test
    void jwt_returnsNonBearerTokenAsIs() {
        // The resolver just removes "Bearer " prefix if present
        TokenRequestContext ctx = mock(TokenRequestContext.class);
        when(ctx.getHeader("Authorization")).thenReturn(Optional.of("Basic credentials"));

        Optional<String> result = jwtResolver.resolve(ctx);
        assertTrue(result.isPresent());
        assertEquals("Basic credentials", result.get());
    }

    @Test
    void jwt_returnsEmptyStringWhenOnlyBearerPrefix() {
        TokenRequestContext ctx = mock(TokenRequestContext.class);
        when(ctx.getHeader("Authorization")).thenReturn(Optional.of("Bearer "));

        Optional<String> result = jwtResolver.resolve(ctx);
        assertTrue(result.isPresent(), "Resolver returns present with empty string");
        assertTrue(result.get().isEmpty(), "Value should be empty string");
    }

    // --- CookieTokenResolver ---

    @Test
    void cookie_extractsAccessToken() {
        TokenRequestContext ctx = mock(TokenRequestContext.class);
        when(ctx.getCookie("access_token")).thenReturn(
            Optional.of(CookieDomain.of("access_token", "my-jwt-token")));

        Optional<String> result = cookieResolver.resolve(ctx);
        assertTrue(result.isPresent());
        assertEquals("my-jwt-token", result.get());
    }

    @Test
    void cookie_returnsEmptyWhenNoCookie() {
        TokenRequestContext ctx = mock(TokenRequestContext.class);
        when(ctx.getCookie("access_token")).thenReturn(Optional.empty());

        assertTrue(cookieResolver.resolve(ctx).isEmpty());
    }

    @Test
    void cookie_returnsEmptyStringValueWhenCookieIsBlank() {
        TokenRequestContext ctx = mock(TokenRequestContext.class);
        when(ctx.getCookie("access_token")).thenReturn(
            Optional.of(CookieDomain.of("access_token", "")));

        Optional<String> result = cookieResolver.resolve(ctx);
        assertTrue(result.isPresent());
        assertTrue(result.get().isEmpty());
    }
}
