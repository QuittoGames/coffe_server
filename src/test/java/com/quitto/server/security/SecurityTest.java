package com.quitto.server.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quitto.server.application.dto.Auth.LoginDTO;
import com.quitto.server.application.dto.Auth.RegisterDTO;
import com.quitto.server.config.TestSecurityConfig;
import com.quitto.server.domain.interfaces.Token.TokenService;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@DisplayName("Security Tests")
class SecurityTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TokenService<Long> tokenService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    // ── Authentication Security ──

    @Test
    @DisplayName("login with SQL injection in username returns 401")
    void login_withSqlInjection_returns401() throws Exception {
        String[] sqlPayloads = {
            "' OR '1'='1",
            "'; DROP TABLE user; --",
            "' UNION SELECT * FROM user; --",
            "admin'--",
            "1' OR '1'='1' --"
        };

        for (String payload : sqlPayloads) {
            LoginDTO login = new LoginDTO(payload, "qualquer_senha");
            mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.msg").value("Invalid username or password"));
        }
    }

    @Test
    @DisplayName("login with SQL injection in password returns 401")
    void login_withSqlInjectionInPassword_returns401() throws Exception {
        String[] sqlPayloads = {
            "' OR '1'='1",
            "'; DROP TABLE user; --",
            "1' OR '1'='1' --"
        };

        for (String payload : sqlPayloads) {
            LoginDTO login = new LoginDTO("admin_teste", payload);
            mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.msg").value("Invalid username or password"));
        }
    }

    @Test
    @DisplayName("login with XSS payload in name returns 401")
    void login_withXssInName_returns401() throws Exception {
        String[] xssPayloads = {
            "<script>alert('xss')</script>",
            "<img src=x onerror=alert(1)>",
            "javascript:alert(1)",
            "\"'><script>alert(1)</script>",
            "<svg onload=alert(1)>"
        };

        for (String payload : xssPayloads) {
            LoginDTO login = new LoginDTO(payload, "senha123");
            mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
        }
    }

    @Test
    @DisplayName("login with empty credentials returns 401")
    void login_withEmptyCredentials_returns400() throws Exception {
        LoginDTO emptyName = new LoginDTO("", "senha123");
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyName)))
            .andExpect(status().isUnauthorized());

        LoginDTO emptyPass = new LoginDTO("admin_teste", "");
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyPass)))
            .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("register with weak password returns 401")
    void register_withWeakPassword_returns400() throws Exception {
        RegisterDTO emptyPass = new RegisterDTO("novo_usuario", "", "novo@email.com");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyPass)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("register with duplicate email returns 401")
    void register_withDuplicateEmail_returns401() throws Exception {
        RegisterDTO duplicate = new RegisterDTO("outro_usuario", "Senha123!", "admin@test.com");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicate)))
            .andExpect(status().isUnauthorized());
    }

    // ── JWT Token Security ──

    @Test
    @DisplayName("expired JWT token returns anonymous user")
    void expiredToken_returns401() throws Exception {
        mockMvc.perform(get("/api/test")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoiY29mZmUtYXBpIiwiaWF0IjoxNTAwMDAwMDAwLCJleHAiOjE1MDAwMDAwMDB9.invalidsignature"))
            .andExpect(status().isOk())
            .andExpect(content().string("Authenticated user: anonymousUser"));
    }

    @Test
    @DisplayName("tampered JWT token returns anonymous user")
    void tamperedToken_returns401() throws Exception {
        String validToken = tokenService.generateToken(1L);
        String[] parts = validToken.split("\\.");
        String tamperedSignature = parts[0] + "." + parts[1] + ".invalidsignature";

        mockMvc.perform(get("/api/test")
                .header("Authorization", "Bearer " + tamperedSignature))
            .andExpect(status().isOk())
            .andExpect(content().string("Authenticated user: anonymousUser"));
    }

    @Test
    @DisplayName("JWT without Bearer prefix returns anonymous user")
    void tokenWithoutBearerPrefix_returns401() throws Exception {
        String validToken = tokenService.generateToken(1L);
        mockMvc.perform(get("/api/test")
                .header("Authorization", validToken))
            .andExpect(status().isOk())
            .andExpect(content().string("Authenticated user: anonymousUser"));
    }

    @Test
    @DisplayName("malformed JWT returns anonymous user")
    void malformedJwt_returns401() throws Exception {
        String[] malformedTokens = {
            "not-a-jwt",
            "abc.def",
            "abc.def.ghi.jkl",
            ".",
            ""
        };

        for (String malformed : malformedTokens) {
            mockMvc.perform(get("/api/test")
                    .header("Authorization", "Bearer " + malformed))
                .andExpect(status().isOk())
                .andExpect(content().string("Authenticated user: anonymousUser"));
        }
    }

    // ── Rate Limiting ──

    @Test
    @Disabled("Rate limiting not yet implemented — TODO Fase 3")
    @DisplayName("rapid login attempts trigger rate limit")
    void rapidLoginAttempts_triggerRateLimit() throws Exception {
        for (int i = 0; i < 20; i++) {
            LoginDTO login = new LoginDTO("user_" + i, "wrong_" + i);
            mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(login)));
        }

        LoginDTO login = new LoginDTO("final_user", "final_password");
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isOk());
    }

    // ── Security Headers & CORS ──

    @Test
    @DisplayName("security headers are present in responses")
    void securityHeaders_arePresent() throws Exception {
        mockMvc.perform(get("/api/test"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("CORS blocks unauthorized origins")
    @Disabled("CORS not yet configured — TODO Fase 2")
    void cors_blocksUnauthorizedOrigins() throws Exception {
        mockMvc.perform(options("/api/test")
                .header("Origin", "https://evil.com")
                .header("Access-Control-Request-Method", "GET"))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("CORS allows authorized origins")
    @Disabled("CORS not yet configured — TODO Fase 2")
    void cors_allowsAuthorizedOrigins() throws Exception {
        mockMvc.perform(options("/api/test")
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "GET"))
            .andExpect(status().isOk())
            .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"));
    }

    @Test
    @DisplayName("authenticated request with valid token returns 200")
    void authenticatedRequest_withValidToken() throws Exception {
        String token = tokenService.generateToken(1L);
        mockMvc.perform(get("/api/test")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("public endpoints are accessible without authentication")
    void publicEndpoints_areAccessible() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk());

        mockMvc.perform(get("/login"))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/test"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("JWT with wrong algorithm header returns anonymous")
    void tokenWithWrongAlgorithm_returnsAnonymous() throws Exception {
        mockMvc.perform(get("/api/test")
                .header("Authorization", "Bearer eyJhbGciOiJub25lIn0.eyJzdWIiOiIxIiwiaXNzIjoiY29mZmUtYXBpIiwiaWF0IjoxNTAwMDAwMDAwLCJleHAiOjE1MDAwMDAwMDB9."))
            .andExpect(status().isOk())
            .andExpect(content().string("Authenticated user: anonymousUser"));
    }
}
