package com.quitto.server.unit.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.quitto.server.application.services.Auth.UserAuthenticationService;
import com.quitto.server.domain.exception.AuthenticationException;
import com.quitto.server.domain.interfaces.Auth.AuthenticationService;
import com.quitto.server.domain.interfaces.Token.TokenService;
import com.quitto.server.domain.models.User.User;

@ExtendWith(MockitoExtension.class)
class UserAuthenticationServiceTest {

    @Mock
    private AuthenticationService authService;

    @Mock
    private TokenService<Long> tokenService;

    @InjectMocks
    private UserAuthenticationService userAuthService;

    @Test
    void login_withValidCredentials_returnsToken() {
        User mockUser = new User(1L, "quitto", "hash", "q@email.com", null);
        when(authService.authenticate("quitto", "senha123")).thenReturn(mockUser);
        when(tokenService.generateToken(1L)).thenReturn("jwt-token");

        String token = userAuthService.login("quitto", "senha123");
        assertEquals("jwt-token", token);
        verify(authService).authenticate("quitto", "senha123");
        verify(tokenService).generateToken(1L);
    }

    @Test
    void login_withInvalidCredentials_throwsAuthenticationException() {
        when(authService.authenticate("quitto", "wrong"))
            .thenThrow(new AuthenticationException("Authentication failed"));

        assertThrows(AuthenticationException.class,
            () -> userAuthService.login("quitto", "wrong"));
        verify(tokenService, never()).generateToken(any());
    }

    @Test
    void register_withValidData_returnsToken() {
        User mockUser = new User(2L, "novo", "hash", "novo@email.com", null);
        when(authService.register("novo", "senha123", "novo@email.com")).thenReturn(mockUser);
        when(tokenService.generateToken(2L)).thenReturn("new-jwt-token");

        String token = userAuthService.register("novo", "senha123", "novo@email.com");
        assertEquals("new-jwt-token", token);
        verify(authService).register("novo", "senha123", "novo@email.com");
        verify(tokenService).generateToken(2L);
    }

    @Test
    void register_whenUsernameExists_throwsException() {
        when(authService.register("existente", "senha123", "email@test.com"))
            .thenThrow(new IllegalArgumentException("Username already exists"));

        assertThrows(IllegalArgumentException.class,
            () -> userAuthService.register("existente", "senha123", "email@test.com"));
        verify(tokenService, never()).generateToken(any());
    }
}
