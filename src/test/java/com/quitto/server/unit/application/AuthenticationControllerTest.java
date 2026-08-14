package com.quitto.server.unit.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quitto.server.application.controllers.Auth.AuthenticationController;
import com.quitto.server.application.dto.ErrorResponse;
import com.quitto.server.application.dto.Auth.LoginDTO;
import com.quitto.server.application.dto.Auth.RegisterDTO;
import com.quitto.server.application.interfaces.Cookies.HttpCookieWriter;
import com.quitto.server.application.services.Auth.UserAuthenticationService;
import com.quitto.server.application.services.Indepotecy.IdepotecyService;
import com.quitto.server.domain.interfaces.Cookies.CookieManager;
import com.quitto.server.domain.interfaces.OperationKey.OperationKey;
import com.quitto.server.domain.valueobject.Cookie.CookieDomain;
import com.quitto.server.infrastructure.config.jackson.OperationKeyDeserializer;

import jakarta.servlet.http.HttpServletResponse;

import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    private MockMvc mockMvc;
    private AuthenticationController controller;

    @Mock
    private UserAuthenticationService userAuthService;

    @Mock
    private CookieManager cookieManager;

    @Mock
    private HttpCookieWriter cookieWriter;

    @Mock
    private IdepotecyService idempotencyService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final JsonMapper jsonMapper = JsonMapper.builder()
            .addModule(new SimpleModule()
                    .addDeserializer(OperationKey.class, new OperationKeyDeserializer()))
            .build();

    @BeforeEach
    void setUp() {
        controller = new AuthenticationController(userAuthService, cookieManager, cookieWriter, idempotencyService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(new JacksonJsonHttpMessageConverter(jsonMapper))
                .build();
    }

    @Test
    void login_withValidCredentials_returns200AndSetsCookie() throws Exception {
        when(userAuthService.login("quitto", "senha123")).thenReturn("jwt-token");
        CookieDomain domain = CookieDomain.of("access_token", "jwt-token");
        when(cookieManager.createAccessTokenCookie("jwt-token")).thenReturn(domain);
        doNothing().when(cookieWriter).writeCookie(any(), any());

        LoginDTO login = new LoginDTO(null,"quitto", "senha123");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isOk())
            .andExpect(content().string(""));

        verify(cookieWriter).writeCookie(any(), eq(domain));
        verify(userAuthService).login("quitto", "senha123");
    }

    @Test
    void login_withInvalidCredentials_returns401() throws Exception {
        when(userAuthService.login("quitto", "wrong"))
            .thenReturn("");

        LoginDTO login = new LoginDTO(null,"quitto", "wrong");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isUnauthorized());

        verifyNoInteractions(cookieManager, cookieWriter);
    }

    @Test
    void register_withValidData_returns200AndSetsCookie() throws Exception {
        when(userAuthService.register("novo", "senha123", "novo@test.com"))
            .thenReturn("new-jwt-token");
        CookieDomain domain = CookieDomain.of("access_token", "new-jwt-token");
        when(cookieManager.createAccessTokenCookie("new-jwt-token")).thenReturn(domain);
        doNothing().when(cookieWriter).writeCookie(any(), any());

        RegisterDTO register = new RegisterDTO(null,"novo", "senha123", "novo@test.com");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
            .andExpect(status().isOk())
            .andExpect(content().string(""));

        verify(cookieWriter).writeCookie(any(), eq(domain));
        verify(userAuthService).register("novo", "senha123", "novo@test.com");
    }

    @Test
    void register_withEmptyPassword_returns401() throws Exception {
        RegisterDTO register = new RegisterDTO(null,"novo", "", "novo@test.com");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void register_withDuplicateUsername_returns401() throws Exception {
        when(userAuthService.register("existente", "senha", "email@test.com"))
            .thenReturn("");

        RegisterDTO register = new RegisterDTO(null,"existente", "senha", "email@test.com");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void login_setsCookieOnResponse() throws Exception {
        when(userAuthService.login("quitto", "senha123")).thenReturn("jwt-token");
        CookieDomain domainCookie = CookieDomain.of("access_token", "jwt-token");
        when(cookieManager.createAccessTokenCookie("jwt-token")).thenReturn(domainCookie);
        doNothing().when(cookieWriter).writeCookie(any(), any());

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginDTO(null,"quitto", "senha123"))))
            .andExpect(status().isOk());

        verify(cookieManager).createAccessTokenCookie("jwt-token");
        verify(cookieWriter).writeCookie(any(), eq(domainCookie));
    }

    @Test
    void login_withDuplicateKey_returns409AndDoesNotProcess() {
        OperationKey key = mock(OperationKey.class);
        when(idempotencyService.isDuplicate(key)).thenReturn(true);

        LoginDTO login = new LoginDTO(key, "quitto", "senha123");

        ResponseEntity<ErrorResponse> response = controller.login(login, mock(HttpServletResponse.class));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verifyNoInteractions(userAuthService, cookieManager, cookieWriter);
        verify(idempotencyService, never()).markProcessed(key);
    }

    @Test
    void login_withNewKey_processesAndMarksProcessed() {
        OperationKey key = mock(OperationKey.class);
        when(idempotencyService.isDuplicate(key)).thenReturn(false);
        when(userAuthService.login("quitto", "senha123")).thenReturn("jwt-token");
        CookieDomain domain = CookieDomain.of("access_token", "jwt-token");
        when(cookieManager.createAccessTokenCookie("jwt-token")).thenReturn(domain);
        doNothing().when(cookieWriter).writeCookie(any(), any());

        LoginDTO login = new LoginDTO(key, "quitto", "senha123");

        ResponseEntity<ErrorResponse> response = controller.login(login, mock(HttpServletResponse.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userAuthService).login("quitto", "senha123");
        verify(cookieWriter).writeCookie(any(), eq(domain));
        verify(idempotencyService).markProcessed(key);
    }

    @Test
    void register_withDuplicateKey_returns409AndDoesNotProcess() {
        OperationKey key = mock(OperationKey.class);
        when(idempotencyService.isDuplicate(key)).thenReturn(true);

        RegisterDTO register = new RegisterDTO(key, "novo", "senha123", "novo@test.com");

        ResponseEntity<Void> response = controller.register(register, mock(HttpServletResponse.class));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verifyNoInteractions(userAuthService, cookieManager, cookieWriter);
        verify(idempotencyService, never()).markProcessed(key);
    }

    @Test
    void register_withNewKey_processesAndMarksProcessed() {
        OperationKey key = mock(OperationKey.class);
        when(idempotencyService.isDuplicate(key)).thenReturn(false);
        when(userAuthService.register("novo", "senha123", "novo@test.com"))
            .thenReturn("new-jwt-token");
        CookieDomain domain = CookieDomain.of("access_token", "new-jwt-token");
        when(cookieManager.createAccessTokenCookie("new-jwt-token")).thenReturn(domain);
        doNothing().when(cookieWriter).writeCookie(any(), any());

        RegisterDTO register = new RegisterDTO(key, "novo", "senha123", "novo@test.com");

        ResponseEntity<Void> response = controller.register(register, mock(HttpServletResponse.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userAuthService).register("novo", "senha123", "novo@test.com");
        verify(cookieWriter).writeCookie(any(), eq(domain));
        verify(idempotencyService).markProcessed(key);
    }

    @Test
    void login_withDuplicateKeyViaJson_returns409AndDoesNotProcess() throws Exception {
        when(idempotencyService.isDuplicate(any(OperationKey.class))).thenReturn(true);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonLoginBody(
                        UUID.fromString("11111111-1111-1111-1111-111111111111"),
                        UUID.fromString("22222222-2222-2222-2222-222222222222"))))
            .andExpect(status().isConflict());

        verifyNoInteractions(userAuthService, cookieManager, cookieWriter);
        verify(idempotencyService, never()).markProcessed(any(OperationKey.class));
    }

    @Test
    void login_withNewKeyViaJson_processesAndMarksProcessed() throws Exception {
        when(idempotencyService.isDuplicate(any(OperationKey.class))).thenReturn(false);
        when(userAuthService.login("quitto", "senha123")).thenReturn("jwt-token");
        CookieDomain domain = CookieDomain.of("access_token", "jwt-token");
        when(cookieManager.createAccessTokenCookie("jwt-token")).thenReturn(domain);
        doNothing().when(cookieWriter).writeCookie(any(), any());

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonLoginBody(
                        UUID.fromString("33333333-3333-3333-3333-333333333333"),
                        UUID.fromString("44444444-4444-4444-4444-444444444444"))))
            .andExpect(status().isOk());

        verify(idempotencyService).markProcessed(any(OperationKey.class));
        verify(userAuthService).login("quitto", "senha123");
        verify(cookieWriter).writeCookie(any(), eq(domain));
    }

    private String jsonLoginBody(UUID id, UUID value) throws Exception {
        return jsonMapper.writeValueAsString(Map.of(
                "idempotencyKey", Map.of("id", id.toString(), "value", value.toString()),
                "name", "quitto",
                "password", "senha123"));
    }
}
