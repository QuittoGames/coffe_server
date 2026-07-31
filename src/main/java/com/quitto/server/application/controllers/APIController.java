package com.quitto.server.application.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class APIController {

    @GetMapping("/test")
    public String test(Authentication authentication) {
        if (authentication == null) {
            return "Authenticated user: anonymousUser";
        }
        return "Authenticated user: " + authentication.getName();
    }
}
