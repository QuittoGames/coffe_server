package com.quitto.server.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quitto.server.application.dto.Auth.LoginDTO;
import com.quitto.server.application.dto.Auth.LoginResponseDTO;
import com.quitto.server.domain.enums.Role;
import com.quitto.server.domain.interfaces.Token.TokenService;
import com.quitto.server.infrastructure.db.User.Entity.UserEntity;
import com.quitto.server.infrastructure.db.User.Repository.JpaUserRepository;

import jakarta.servlet.http.Cookie;

@SpringBootTest
@ActiveProfiles("test")
class LoginIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService<Long> tokenService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String USERNAME = "login_test_user";
    private static final String PASSWORD = "SenhaForte123!";
    private static final String EMAIL = "login_test@email.com";

    private Long savedUserId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        var entity = new UserEntity(
            null, USERNAME,
            passwordEncoder.encode(PASSWORD),
            EMAIL, Role.USER
        );
        savedUserId = userRepository.save(entity).getId();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteById(savedUserId);
    }

    @Test
    void login_withValidCredentials_returns200WithToken() throws Exception {
        LoginDTO login = new LoginDTO(USERNAME, PASSWORD);

        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isOk())
            .andReturn();

        String body = result.getResponse().getContentAsString();
        LoginResponseDTO response = objectMapper.readValue(body, LoginResponseDTO.class);

        assertNotNull(response.token());
        assertFalse(response.token().isBlank());
        assertNotNull(response.date());
    }

    @Test
    void login_returnsValidJwtToken() throws Exception {
        LoginDTO login = new LoginDTO(USERNAME, PASSWORD);

        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isOk())
            .andReturn();

        LoginResponseDTO response = objectMapper.readValue(
            result.getResponse().getContentAsString(), LoginResponseDTO.class);

        assertTrue(tokenService.verifyToken(response.token()));
        var extractedId = tokenService.extractIdSubject(response.token());
        assertTrue(extractedId.isPresent());
        assertEquals(savedUserId, extractedId.get());
    }

    @Test
    void login_setsCookieOnResponse() throws Exception {
        LoginDTO login = new LoginDTO(USERNAME, PASSWORD);

        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isOk())
            .andReturn();

        Cookie accessTokenCookie = result.getResponse().getCookie("access_token");
        assertNotNull(accessTokenCookie, "access_token cookie must be present");
        assertFalse(accessTokenCookie.getValue().isBlank());
    }

    @Test
    void login_setsCookieWithHttpOnlyAndSecure() throws Exception {
        LoginDTO login = new LoginDTO(USERNAME, PASSWORD);

        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isOk())
            .andReturn();

        Cookie cookie = result.getResponse().getCookie("access_token");
        assertNotNull(cookie);
        assertTrue(cookie.isHttpOnly(), "Cookie must be httpOnly");
        // Secure depends on app.cookie.secure property (false on HTTP/dev, true on HTTPS/prod)
        assertFalse(cookie.getSecure(), "Cookie should NOT be secure on HTTP (test profile)");
    }

    @Test
    void login_withWrongPassword_returns401() throws Exception {
        LoginDTO login = new LoginDTO(USERNAME, "wrong-password");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void login_withNonExistentUser_returns401() throws Exception {
        LoginDTO login = new LoginDTO("nonexistent_user", PASSWORD);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void login_withEmptyBody_returns401() throws Exception {
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void login_withMalformedJson_returns400() throws Exception {
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{malformed"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void login_cookieContainsSameTokenAsResponseBody() throws Exception {
        LoginDTO login = new LoginDTO(USERNAME, PASSWORD);

        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isOk())
            .andReturn();

        LoginResponseDTO response = objectMapper.readValue(
            result.getResponse().getContentAsString(), LoginResponseDTO.class);

        Cookie cookie = result.getResponse().getCookie("access_token");
        assertNotNull(cookie);
        assertEquals(response.token(), cookie.getValue(),
            "Cookie value must match the JWT from response body");
    }
}
