package com.quitto.server.unit.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

import com.quitto.server.domain.valueobject.Cookie.CookieDomain;
import com.quitto.server.infrastructure.Mappers.Cookies.CookieMapper;

/**
 * Testa a conversão CookieDomain -> ResponseCookie (fronteira Spring).
 * O mapper aplica SameSite=Lax e NÃO seta Domain (cookie host-only).
 */
class CookieMapperTest {

    private final CookieMapper mapper = new CookieMapper();

    @Test
    void mapsNameValueAndFlags() {
        ResponseCookie cookie = mapper.toFrameworkCookie(
            CookieDomain.of("access_token", "jwt-123")).build();

        assertEquals("access_token", cookie.getName());
        assertEquals("jwt-123", cookie.getValue());
        assertTrue(cookie.isHttpOnly());
        assertTrue(cookie.isSecure());
        assertEquals("/", cookie.getPath());
    }

    @Test
    void appliesSameSiteLax() {
        ResponseCookie cookie = mapper.toFrameworkCookie(
            CookieDomain.of("access_token", "jwt-123")).build();

        assertEquals("Lax", cookie.getSameSite(),
            "mapper must apply SameSite=Lax (postura anti-CSRF do sistema)");
    }

    @Test
    void doesNotSetDomain_cookieStaysHostOnly() {
        ResponseCookie cookie = mapper.toFrameworkCookie(
            CookieDomain.of("access_token", "jwt-123")).build();

        assertNull(cookie.getDomain(),
            "mapper must NOT set Domain (host-only cookie, sem escopo alargado)");
    }

    @Test
    void mapsFalseFlagsToo() {
        ResponseCookie cookie = mapper.toFrameworkCookie(
            new CookieDomain("x", "y", false, false, "/api", 60)).build();

        assertFalse(cookie.isHttpOnly());
        assertFalse(cookie.isSecure());
        assertEquals("/api", cookie.getPath());
    }
}
