package com.quitto.server.unit.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.quitto.server.domain.valueobject.CookieDomain;
import com.quitto.server.infrastructure.services.Auth.Token.Cookies.HttpCookieService;

class HttpCookieServiceTest {

    private final HttpCookieService service = new HttpCookieService(false);

    @Test
    void createsCookie() {
        CookieDomain cookie = service.createCookie("access_token", "jwt-value");
        assertEquals("access_token", cookie.name());
        assertEquals("jwt-value", cookie.value());
        assertTrue(cookie.httpOnly());
        assertFalse(cookie.secure());
        assertEquals("/", cookie.path());
        assertNull(cookie.maxAge());
    }

    @Test
    void createsCookieWithPathAndMaxAge() {
        CookieDomain cookie = service.createCookie("refresh_token", "val", "/auth/refresh", 604800);
        assertEquals("refresh_token", cookie.name());
        assertEquals("/auth/refresh", cookie.path());
        assertEquals(604800, cookie.maxAge());
    }

}
