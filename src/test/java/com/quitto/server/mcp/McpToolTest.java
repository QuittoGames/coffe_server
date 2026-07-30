package com.quitto.server.mcp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.google.api.services.calendar.model.Event;
import com.quitto.server.infrastructure.external.google.GoogleAuthService;
import com.quitto.server.mcp.services.GoogleCalendarService;
import com.quitto.server.mcp.tools.GoogleCalendarTools;

@ExtendWith(MockitoExtension.class)
@DisplayName("MCP Tool Tests")
class McpToolTest {

    @Mock
    private GoogleCalendarService calendarService;

    @Mock
    private GoogleAuthService authService;

    @Mock
    private OAuth2AuthenticationToken oauthToken;

    @Mock
    private OAuth2AuthorizedClient authorizedClient;

    private GoogleCalendarTools tools;

    @BeforeEach
    void setUp() {
        tools = new GoogleCalendarTools();
        tools.service = calendarService;
        tools.authService = authService;
    }

    @Test
    @DisplayName("listEvents returns empty list when not authenticated")
    void listEvents_whenNotAuthenticated_returnsEmptyList() {
        SecurityContextHolder.clearContext();
        List<Event> result = tools.listEvents();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("listEvents returns empty when auth is not OAuth2")
    void listEvents_whenAuthenticationNotOAuth2_returnsEmptyList() {
        Authentication nonOAuthAuth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(nonOAuthAuth);
        SecurityContextHolder.setContext(securityContext);

        List<Event> result = tools.listEvents();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("listEvents handles auth service failure gracefully")
    void listEvents_whenAuthServiceFails_returnsEmptyList() {
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(oauthToken);
        SecurityContextHolder.setContext(securityContext);

        when(authService.getAuthorizedClient(oauthToken))
            .thenThrow(new IllegalStateException("Token expirado"));

        List<Event> result = tools.listEvents();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("listEvents returns events when OAuth2 authenticated")
    void listEvents_whenOAuth2Authenticated_returnsEvents() throws Exception {
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(oauthToken);
        SecurityContextHolder.setContext(securityContext);

        when(authService.getAuthorizedClient(oauthToken)).thenReturn(authorizedClient);

        List<Event> mockEvents = new ArrayList<>();
        mockEvents.add(new Event().setSummary("Reuniao"));
        mockEvents.add(new Event().setSummary("Review"));

        when(calendarService.listEvents(authorizedClient)).thenReturn(mockEvents);

        List<Event> result = tools.listEvents();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Reuniao", result.get(0).getSummary());
        assertEquals("Review", result.get(1).getSummary());

        verify(calendarService).listEvents(authorizedClient);
    }

    @Test
    @DisplayName("listEvents handles service exception gracefully")
    void listEvents_whenServiceThrows_returnsEmptyList() throws Exception {
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(oauthToken);
        SecurityContextHolder.setContext(securityContext);

        when(authService.getAuthorizedClient(oauthToken)).thenReturn(authorizedClient);
        when(calendarService.listEvents(authorizedClient))
            .thenThrow(new RuntimeException("API error"));

        List<Event> result = tools.listEvents();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("createEvent returns empty string (stub)")
    void createEvent_returnsEmptyString() {
        GoogleCalendarService service = new GoogleCalendarService();
        assertEquals("", service.createEvent("Titulo", "Descricao"));
        assertEquals("", service.createEvent(null, null));
    }
}
