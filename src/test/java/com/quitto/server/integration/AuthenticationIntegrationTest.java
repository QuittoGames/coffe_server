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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quitto.server.application.dto.Auth.LoginDTO;
import com.quitto.server.application.dto.Auth.LoginResponseDTO;
import com.quitto.server.domain.enums.Role;
import com.quitto.server.domain.interfaces.Token.TokenService;
import com.quitto.server.infrastructure.db.User.Entity.UserEntity;
import com.quitto.server.infrastructure.db.User.Repository.JpaUserRepository;
import com.quitto.server.infrastructure.security.Filter.Adapter.HttpTokenRequestContext;
import com.quitto.server.infrastructure.security.Filter.Token.CookieTokenResolver;
import com.quitto.server.infrastructure.security.Filter.Token.JwtTokenResolver;
import com.quitto.server.infrastructure.services.Auth.Token.TokenResolverManager;

import jakarta.servlet.http.Cookie;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class AuthenticationIntegrationTest {

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

    private static final String USERNAME = "auth_flow_user";
    private static final String PASSWORD = "FlowPass123!";
    private static final String EMAIL = "auth_flow@email.com";
    private Long savedUserId;

    private final JwtTokenResolver jwtResolver = new JwtTokenResolver();
    private final CookieTokenResolver cookieResolver = new CookieTokenResolver();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        var entity = new UserEntity(null, USERNAME,
            passwordEncoder.encode(PASSWORD), EMAIL, Role.USER);
        savedUserId = userRepository.save(entity).getId();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteById(savedUserId);
    }

    @Test
    void fullFlow_loginReturnsValidJwt() throws Exception {
        LoginDTO login = new LoginDTO(USERNAME, PASSWORD);

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isOk())
            .andReturn();

        LoginResponseDTO response = objectMapper.readValue(
            loginResult.getResponse().getContentAsString(),
            LoginResponseDTO.class);

        String jwt = response.token();
        assertTrue(tokenService.verifyToken(jwt));
        Long extractedId = tokenService.extractIdSubject(jwt).orElseThrow();
        assertEquals(savedUserId, extractedId);
    }

    @Test
    void loginResponseIncludesCookie() throws Exception {
        LoginDTO login = new LoginDTO(USERNAME, PASSWORD);

        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isOk())
            .andReturn();

        Cookie cookie = result.getResponse().getCookie("access_token");
        assertNotNull(cookie);
        assertFalse(cookie.getValue().isBlank());
    }

    @Test
    void cookieTokenCanBeResolvedByCookieResolver() throws Exception {
        LoginDTO login = new LoginDTO(USERNAME, PASSWORD);

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isOk())
            .andReturn();

        Cookie accessCookie = loginResult.getResponse().getCookie("access_token");
        assertNotNull(accessCookie);

        var manager = new TokenResolverManager(
            List.of(cookieResolver, jwtResolver));

        var request = new MockHttpServletRequest();
        request.setCookies(new Cookie("access_token", accessCookie.getValue()));
        var ctx = new HttpTokenRequestContext(request);

        var resolved = manager.resolve(ctx);
        assertTrue(resolved.isPresent());
        assertEquals(accessCookie.getValue(), resolved.get());
        assertTrue(tokenService.verifyToken(resolved.get()));
    }

    @Test
    void bearerTokenCanBeResolvedByJwtResolver() throws Exception {
        LoginDTO login = new LoginDTO(USERNAME, PASSWORD);

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isOk())
            .andReturn();

        LoginResponseDTO response = objectMapper.readValue(
            loginResult.getResponse().getContentAsString(),
            LoginResponseDTO.class);

        var manager = new TokenResolverManager(List.of(cookieResolver, jwtResolver));

        var request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + response.token());
        var ctx = new HttpTokenRequestContext(request);

        var resolved = manager.resolve(ctx);
        assertTrue(resolved.isPresent());
        assertEquals(response.token(), resolved.get());
    }

    @Test
    void tokenService_generatesAndVerifiesToken() {
        String token = tokenService.generateToken(savedUserId);
        assertNotNull(token);
        assertTrue(tokenService.verifyToken(token));

        Long extracted = tokenService.extractIdSubject(token).orElseThrow();
        assertEquals(savedUserId, extracted);
    }

    @Test
    void tokenService_rejectsInvalidToken() {
        assertFalse(tokenService.verifyToken("invalid-token"));
        assertThrows(JWTDecodeException.class,
            () -> tokenService.extractIdSubject("invalid-token"));
    }

    @Test
    void httpTokenRequestContext_resolvesCookieAndHeader() {
        var request = new MockHttpServletRequest();
        request.setCookies(
            new Cookie("access_token", "cookie-val"),
            new Cookie("refresh_token", "refresh-val")
        );
        request.addHeader("Authorization", "Bearer header-val");

        var ctx = new HttpTokenRequestContext(request);

        var cookieResult = ctx.getCookie("access_token");
        assertTrue(cookieResult.isPresent());
        assertEquals("cookie-val", cookieResult.get().value());

        var headerResult = ctx.getHeader("Authorization");
        assertTrue(headerResult.isPresent());
        assertEquals("Bearer header-val", headerResult.get());
    }

    @Test
    void loginTokenMatchesCookieValue() throws Exception {
        LoginDTO login = new LoginDTO(USERNAME, PASSWORD);

        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isOk())
            .andReturn();

        LoginResponseDTO response = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            LoginResponseDTO.class);

        Cookie cookie = result.getResponse().getCookie("access_token");
        assertNotNull(cookie);
        assertEquals(response.token(), cookie.getValue());
    }

    @Test
    void managerWithCookieAndJwt_prefersCookie() throws Exception {
        var manager = new TokenResolverManager(List.of(cookieResolver, jwtResolver));

        var request = new MockHttpServletRequest();
        request.setCookies(new Cookie("access_token", "cookie-token"));
        request.addHeader("Authorization", "Bearer header-token");

        var resolved = manager.resolve(new HttpTokenRequestContext(request));
        assertTrue(resolved.isPresent());
        assertEquals("cookie-token", resolved.get());
    }
}
