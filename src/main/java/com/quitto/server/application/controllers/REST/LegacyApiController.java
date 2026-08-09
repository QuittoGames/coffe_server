package com.quitto.server.application.controllers.REST;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LegacyApiController {

    /**
     * Legacy health/session check endpoint for frontend compatibility.
     * Permitted for all (see SecurityConfig).
     */
    @GetMapping("/test")
    public Map<String, String> test(Authentication authentication) {
        String name = authentication != null ? authentication.getName() : "anonymous";
        return Map.of("user", name);
    }
}