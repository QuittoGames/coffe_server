package com.quitto.server;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;

import jakarta.servlet.http.Cookie;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private String loginAndGetToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "admin_teste", "password": "123"}
                        """))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        return JsonPath.read(json, "$.token");
    }

    @Test
    void login_withValidCredentials_returnsToken() throws Exception {
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "admin_teste", "password": "123"}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.date").isNotEmpty());
    }

    @Test
    void login_withInvalidPassword_returns401() throws Exception {
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "admin_teste", "password": "wrong"}
                        """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.msg").value("Invalid username or password"));
    }

    @Test
    void login_withInvalidUsername_returns401() throws Exception {
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "nonexistent", "password": "123"}
                        """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.msg").value("Invalid username or password"));
    }

    @Test
    void accessApiTest_withBearerToken_authenticatesUser() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(get("/api/test")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Authenticated user: admin_teste"));
    }

    @Test
    void accessApiTest_withCookieToken_authenticatesUser() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(get("/api/test")
                .cookie(new Cookie("access_token", token)))
                .andExpect(status().isOk())
                .andExpect(content().string("Authenticated user: admin_teste"));
    }

    @Test
    void accessApiTest_withoutToken_showsAnonymous() throws Exception {
        mockMvc.perform(get("/api/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("Authenticated user: anonymousUser"));
    }

    @Test
    void accessApiTest_withInvalidCookie_showsAnonymous() throws Exception {
        mockMvc.perform(get("/api/test")
                .cookie(new Cookie("access_token", "invalid-jwt-token")))
                .andExpect(status().isOk())
                .andExpect(content().string("Authenticated user: anonymousUser"));
    }

    @Test
    void accessApiTest_withBearerTokenAndCookie_bearerTakesPrecedence() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(get("/api/test")
                .header("Authorization", "Bearer " + token)
                .cookie(new Cookie("access_token", "invalid-cookie-token")))
                .andExpect(status().isOk())
                .andExpect(content().string("Authenticated user: admin_teste"));
    }
}
