package com.quitto.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quitto.server.application.dto.Auth.LoginDTO;
import com.quitto.server.application.dto.Auth.LoginResponseDTO;
import com.quitto.server.domain.enums.Role;
import com.quitto.server.domain.interfaces.Token.TokenService;
import com.quitto.server.domain.valueobject.CookieDomain;
import com.quitto.server.infrastructure.db.User.Entity.UserEntity;
import com.quitto.server.infrastructure.db.User.Repository.JpaUserRepository;
import com.quitto.server.infrastructure.security.Filter.Adapter.HttpTokenRequestContext;
import com.quitto.server.infrastructure.security.Filter.JwtAuthenticationFilter;
import com.quitto.server.infrastructure.security.Filter.Token.CookieTokenResolver;
import com.quitto.server.infrastructure.security.Filter.Token.JwtTokenResolver;
import com.quitto.server.infrastructure.services.Auth.Token.Cookies.HttpCookieService;
import com.quitto.server.infrastructure.services.Auth.Token.TokenResolverManager;

import jakarta.servlet.http.Cookie;

@SpringBootTest
@ActiveProfiles("test")
class CookieSystemIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService<Long> tokenService;

    @Autowired
    private HttpCookieService httpCookieService;

    @Autowired
    private TokenResolverManager resolverManager;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtTokenResolver jwtTokenResolver = new JwtTokenResolver();
    private final CookieTokenResolver cookieTokenResolver = new CookieTokenResolver();

    private static final String TEST_USER = "integracao_teste";
    private static final String TEST_PASSWORD = "senha123";
    private static final String TEST_EMAIL = "integracao@test.com";

    private Long savedUserId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        var entity = new UserEntity(
            null,
            TEST_USER,
            passwordEncoder.encode(TEST_PASSWORD),
            TEST_EMAIL,
            Role.ADMIN
        );
        var saved = jpaUserRepository.save(entity);
        savedUserId = saved.getId();
    }

    @AfterEach
    void tearDown() {
        jpaUserRepository.deleteById(savedUserId);
    }

    @Test
    void login_withValidCredentials_returnsJwtToken() throws Exception {
        LoginDTO login = new LoginDTO(TEST_USER, TEST_PASSWORD);

        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isOk())
            .andReturn();

        String body = result.getResponse().getContentAsString();
        LoginResponseDTO response = objectMapper.readValue(body, LoginResponseDTO.class);

        assertNotNull(response.token());
        assertFalse(response.token().isBlank());
    }

    @Test
    void login_withInvalidCredentials_returnsUnauthorized() throws Exception {
        LoginDTO login = new LoginDTO(TEST_USER, "wrong-password");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void jwtTokenService_generatesAndVerifiesToken() {
        String token = tokenService.generateToken(savedUserId);

        assertNotNull(token);
        assertFalse(token.isBlank());
        assertTrue(tokenService.verifyToken(token));

        Long extractedId = tokenService.extractIdSubject(token).orElseThrow();
        assertEquals(savedUserId, extractedId);
    }

    @Test
    void jwtTokenService_rejectsInvalidToken() {
        assertThrows(com.auth0.jwt.exceptions.JWTDecodeException.class,
            () -> tokenService.verifyToken("invalid-token"));
    }

    @Test
    void httpCookieService_createsDomainCookie() {
        CookieDomain cookie = httpCookieService.createCookie("access_token", "jwt-value");

        assertEquals("access_token", cookie.name());
        assertEquals("jwt-value", cookie.value());
        assertTrue(cookie.httpOnly());
        assertFalse(cookie.secure());
        assertEquals("/", cookie.path());
    }

    @Test
    void httpCookieService_createsDomainCookieWithCustomPath() {
        CookieDomain cookie = httpCookieService.createCookie("refresh_token", "val", "/api", 7200);

        assertEquals("refresh_token", cookie.name());
        assertEquals("/api", cookie.path());
        assertEquals(7200, cookie.maxAge());
    }

    @Test
    void cookieTokenResolver_extractsTokenFromCookie() {
        var req = new MockHttpServletRequest();
        req.setCookies(new Cookie("access_token", "my-jwt-token"));
        var ctx = new HttpTokenRequestContext(req);

        var result = cookieTokenResolver.resolve(ctx);

        assertTrue(result.isPresent());
        assertEquals("my-jwt-token", result.get());
    }

    @Test
    void jtwTokenResvoler_extractsBearerToken() {
        var req = new MockHttpServletRequest();
        req.addHeader("Authorization", "Bearer my-bearer-jwt");
        var ctx = new HttpTokenRequestContext(req);

        var result = jwtTokenResolver.resolve(ctx);

        assertTrue(result.isPresent());
        assertEquals("my-bearer-jwt", result.get());
    }

    @Test
    void tokenResolverManager_chain_usesCookieFirst() {
        var manager = new TokenResolverManager(List.of(cookieTokenResolver, jwtTokenResolver));
        var req = new MockHttpServletRequest();
        req.setCookies(new Cookie("access_token", "token-from-cookie"));
        var ctx = new HttpTokenRequestContext(req);

        var result = manager.resolve(ctx);

        assertTrue(result.isPresent());
        assertEquals("token-from-cookie", result.get());
    }

    @Test
    void tokenResolverManager_chain_fallsbackToBearer() {
        var manager = new TokenResolverManager(List.of(cookieTokenResolver, jwtTokenResolver));
        var req = new MockHttpServletRequest();
        req.addHeader("Authorization", "Bearer token-from-header");
        var ctx = new HttpTokenRequestContext(req);

        var result = manager.resolve(ctx);

        assertTrue(result.isPresent());
        assertEquals("token-from-header", result.get());
    }

    @Test
    void tokenResolverManager_chain_returnsEmptyWhenNoToken() {
        var manager = new TokenResolverManager(List.of(cookieTokenResolver, jwtTokenResolver));
        var ctx = new HttpTokenRequestContext(new MockHttpServletRequest());

        var result = manager.resolve(ctx);

        assertTrue(result.isEmpty());
    }

    @Test
    void jwtAuthenticationFilter_recoverToken_succeedsWithBearerHeader() {
        var request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer some-token");

        String result = jwtAuthenticationFilter.recoverToken(request);
        assertEquals("some-token", result);
    }

    @Test
    void recoverToken_withProperManagerAndCookie() {
        var manager = new TokenResolverManager(List.of(cookieTokenResolver, jwtTokenResolver));
        String jwt = tokenService.generateToken(savedUserId);

        var request = new MockHttpServletRequest();
        request.setCookies(new Cookie("access_token", jwt));

        var context = new HttpTokenRequestContext(request);
        var result = manager.resolve(context);

        assertTrue(result.isPresent());
        assertEquals(jwt, result.get());
    }

    @Test
    void recoverToken_withProperManagerAndBearer() {
        var manager = new TokenResolverManager(List.of(cookieTokenResolver, jwtTokenResolver));
        String jwt = tokenService.generateToken(savedUserId);

        var request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + jwt);

        var context = new HttpTokenRequestContext(request);
        var result = manager.resolve(context);

        assertTrue(result.isPresent());
        assertEquals(jwt, result.get());
    }

    @Test
    void fullFlow_loginAndUseGeneratedToken() throws Exception {
        LoginDTO login = new LoginDTO(TEST_USER, TEST_PASSWORD);

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isOk())
            .andReturn();

        LoginResponseDTO response = objectMapper.readValue(
            loginResult.getResponse().getContentAsString(),
            LoginResponseDTO.class
        );

        assertTrue(tokenService.verifyToken(response.token()));

        Long extractedId = tokenService.extractIdSubject(response.token()).orElseThrow();
        assertEquals(savedUserId, extractedId);
    }

    @Test
    void httpTokenRequestContext_readsCookieCorrectly() {
        var request = new MockHttpServletRequest();
        request.setCookies(
            new Cookie("access_token", "token1"),
            new Cookie("refresh_token", "token2")
        );
        var ctx = new HttpTokenRequestContext(request);

        var access = ctx.getCookie("access_token");
        assertTrue(access.isPresent());
        assertEquals("token1", access.get().value());

        var refresh = ctx.getCookie("refresh_token");
        assertTrue(refresh.isPresent());
        assertEquals("token2", refresh.get().value());

        var nonExistent = ctx.getCookie("nonexistent");
        assertTrue(nonExistent.isEmpty());
    }

    @Test
    void httpTokenRequestContext_returnsEmptyWhenNoCookies() {
        var ctx = new HttpTokenRequestContext(new MockHttpServletRequest());

        var result = ctx.getCookie("access_token");
        assertTrue(result.isEmpty());
    }

    @Test
    void cookieConfig_hasRequiredBeans() {
        assertNotNull(tokenService);
        assertNotNull(httpCookieService);
        assertNotNull(resolverManager);
        assertNotNull(jwtAuthenticationFilter);
    }
}
