package com.quitto.server.application.controllers;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class APIController {

    /**
     * Health/session check. Requires authentication (see SecurityConfig), so an
     * anonymous request hits the entry point and returns 401 — the frontend uses
     * this to validate the HttpOnly-cookie session.
     */
    @GetMapping("/test")
    public Map<String, String> test(Authentication authentication) {
        String name = authentication != null ? authentication.getName() : "anonymous";
        return Map.of("user", name);
    }
}
