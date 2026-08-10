package com.quitto.server.unit.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.quitto.server.domain.valueobject.Cookie.CookieDomain;

class CookieDomainTest {

    @Test
    void createsWithDefaults() {
        CookieDomain cookie = CookieDomain.of("access_token", "jwt-value");
        assertEquals("access_token", cookie.name());
        assertEquals("jwt-value", cookie.value());
        assertTrue(cookie.httpOnly());
        assertTrue(cookie.secure());
        assertEquals("/", cookie.path());
        assertNull(cookie.maxAge());
    }

    @Test
    void createsWithPathAndMaxAge() {
        CookieDomain cookie = CookieDomain.of("refresh_token", "val", "/auth/refresh", 604800);
        assertEquals("/auth/refresh", cookie.path());
        assertEquals(604800, cookie.maxAge());
    }

    @Test
    void rejectsNullName() {
        assertThrows(NullPointerException.class,
            () -> new CookieDomain(null, "value", true, true, "/", null));
    }

    @Test
    void rejectsNullValue() {
        assertThrows(NullPointerException.class,
            () -> new CookieDomain("name", null, true, true, "/", null));
    }

    @Test
    void rejectsNameWithEquals() {
        assertThrows(IllegalArgumentException.class,
            () -> CookieDomain.of("bad=name", "value"));
    }

    @Test
    void rejectsNameWithSemicolon() {
        assertThrows(IllegalArgumentException.class,
            () -> CookieDomain.of("bad;name", "value"));
    }

    @Test
    void rejectsNameWithComma() {
        assertThrows(IllegalArgumentException.class,
            () -> CookieDomain.of("bad,name", "value"));
    }

    @Test
    void rejectsEmptyName() {
        assertThrows(IllegalArgumentException.class,
            () -> CookieDomain.of("", "value"));
    }

    @Test
    void allowsCustomHttpOnlyAndSecure() {
        CookieDomain cookie = new CookieDomain("test", "val", false, false, "/custom", 100);
        assertFalse(cookie.httpOnly());
        assertFalse(cookie.secure());
        assertEquals("/custom", cookie.path());
        assertEquals(100, cookie.maxAge().intValue());
    }
}
