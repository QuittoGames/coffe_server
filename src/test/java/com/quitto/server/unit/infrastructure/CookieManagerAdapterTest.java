package com.quitto.server.unit.infrastructure;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.quitto.server.domain.interfaces.Cookies.CookieFactory;
import com.quitto.server.domain.valueobject.CookieDomain;
import com.quitto.server.infrastructure.Adpter.out.Auth.CookieManagerAdapter;

@ExtendWith(MockitoExtension.class)
class CookieManagerAdapterTest {

    @Mock
    private CookieFactory cookieService;

    @InjectMocks
    private CookieManagerAdapter adapter;

    @Test
    void createAccessTokenCookie_delegatesToCookieService() {
        CookieDomain expected = CookieDomain.of("access_token", "jwt-123");
        when(cookieService.createCookie("access_token", "jwt-123")).thenReturn(expected);

        CookieDomain result = adapter.createAccessTokenCookie("jwt-123");

        assertEquals(expected, result);
        assertEquals("access_token", result.name());
        assertEquals("jwt-123", result.value());
        verify(cookieService).createCookie("access_token", "jwt-123");
    }

    @Test
    void createAccessTokenCookie_preservesDefaults() {
        CookieDomain mockDomain = CookieDomain.of("access_token", "token-value");
        when(cookieService.createCookie("access_token", "token-value")).thenReturn(mockDomain);

        CookieDomain result = adapter.createAccessTokenCookie("token-value");

        assertTrue(result.httpOnly());
        assertTrue(result.secure());
        assertEquals("/", result.path());
        assertNull(result.maxAge());
    }
}
