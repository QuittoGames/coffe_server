package com.quitto.server.unit.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;

import com.quitto.server.domain.valueobject.Cookie.CookieDomain;
import com.quitto.server.infrastructure.Mappers.Cookies.CookieMapper;
import com.quitto.server.infrastructure.services.Auth.Token.Cookies.HttpCookieWriterManager;

/**
 * Testa a fronteira real de escrita: CookieDomain (domínio) -> header Set-Cookie
 * no HttpServletResponse. Usa o CookieMapper real (objeto simples, sem mock) e
 * MockHttpServletResponse do spring-test.
 */
class HttpCookieWriterManagerTest {

    private final HttpCookieWriterManager writer = new HttpCookieWriterManager(new CookieMapper());

    @Test
    void writeCookie_writesSetCookieHeaderWithAllFlags() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        CookieDomain cookie = CookieDomain.of("access_token", "jwt-123");

        writer.writeCookie(response, cookie);

        String header = response.getHeader(HttpHeaders.SET_COOKIE);
        assertNotNull(header, "Set-Cookie header must be present");
        assertTrue(header.contains("access_token=jwt-123"));
        assertTrue(header.contains("HttpOnly"), "header must carry HttpOnly");
        assertTrue(header.contains("Secure"), "header must carry Secure (factory default)");
        assertTrue(header.contains("Path=/"), "header must carry Path=/");
        assertTrue(header.contains("SameSite=Lax"), "header must carry SameSite=Lax");
    }

    @Test
    void writeCookie_omitsMaxAgeWhenNull_producingSessionCookie() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        CookieDomain sessionCookie = CookieDomain.of("access_token", "jwt-123");

        writer.writeCookie(response, sessionCookie);

        String header = response.getHeader(HttpHeaders.SET_COOKIE);
        assertNotNull(header);
        assertFalse(header.contains("Max-Age"),
            "cookie without maxAge must NOT emit Max-Age (session cookie)");
    }

    @Test
    void writeCookie_includesMaxAgeWhenPresent() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        CookieDomain persistent = CookieDomain.of("access_token", "jwt-123", "/", 3600);

        writer.writeCookie(response, persistent);

        String header = response.getHeader(HttpHeaders.SET_COOKIE);
        assertNotNull(header);
        assertTrue(header.contains("Max-Age=3600"), "header must carry Max-Age=3600");
    }

    @Test
    void writeCookie_writesRevocationCookieWithMaxAgeZero() {
        // Contrato usado pelo /auth/logout: valor vazio + Max-Age=0 apaga no browser
        MockHttpServletResponse response = new MockHttpServletResponse();
        CookieDomain revoked = CookieDomain.of("access_token", "", "/", 0);

        writer.writeCookie(response, revoked);

        String header = response.getHeader(HttpHeaders.SET_COOKIE);
        assertNotNull(header);
        assertTrue(header.contains("access_token="), "revoked cookie keeps the same name");
        assertTrue(header.contains("Max-Age=0"), "revocation must emit Max-Age=0");
    }

    @Test
    void writeCookie_throwsWhenResponseIsNull() {
        CookieDomain cookie = CookieDomain.of("access_token", "jwt-123");

        NullPointerException ex = assertThrows(NullPointerException.class,
            () -> writer.writeCookie(null, cookie));
        assertEquals("response cannot be null", ex.getMessage());
    }

    @Test
    void writeCookie_throwsWhenCookieDomainIsNull() {
        MockHttpServletResponse response = new MockHttpServletResponse();

        NullPointerException ex = assertThrows(NullPointerException.class,
            () -> writer.writeCookie(response, null));
        assertEquals("cookieDomain cannot be null", ex.getMessage());
    }
}
