package com.quitto.server.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import jakarta.servlet.http.Cookie;

/**
 * Cobre o endpoint POST /auth/logout — revogação do cookie access_token
 * via Set-Cookie com valor vazio e Max-Age=0 (gap que não tinha teste).
 * /auth/** é público (SecurityConfig), então não precisa de usuário no banco.
 */
@SpringBootTest
@ActiveProfiles("test")
class LogoutIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void logout_returns200() throws Exception {
        mockMvc.perform(post("/auth/logout"))
            .andExpect(status().isOk());
    }

    @Test
    void logout_revokesCookieWithMaxAgeZero() throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/logout"))
            .andExpect(status().isOk())
            .andReturn();

        Cookie cookie = result.getResponse().getCookie("access_token");
        assertNotNull(cookie, "logout must emit an access_token cookie");
        assertEquals("", cookie.getValue(), "revoked cookie must have empty value");
        assertEquals(0, cookie.getMaxAge(), "revoked cookie must have Max-Age=0");
    }

    @Test
    void logout_setCookieHeaderKeepsSecurityFlags() throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/logout"))
            .andExpect(status().isOk())
            .andReturn();

        String header = result.getResponse().getHeader(HttpHeaders.SET_COOKIE);
        assertNotNull(header, "Set-Cookie header must be present");
        assertTrue(header.contains("access_token="), "header must target access_token");
        assertTrue(header.contains("Max-Age=0"), "header must revoke with Max-Age=0");
        assertTrue(header.contains("HttpOnly"), "revoked cookie must keep HttpOnly");
        assertTrue(header.contains("Path=/"), "revoked cookie must keep Path=/ (mesmo path da criação)");
        assertTrue(header.contains("SameSite=Lax"), "revoked cookie must keep SameSite=Lax");
    }
}
