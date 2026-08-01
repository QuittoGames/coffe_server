package com.quitto.server.application.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quitto.server.application.dto.Auth.LoginDTO;
import com.quitto.server.application.dto.Auth.RegisterDTO;
import com.quitto.server.application.interfaces.Cookies.HttpCookieWriter;
import com.quitto.server.application.services.Auth.UserAuthenticationService;
import com.quitto.server.domain.interfaces.Cookies.CookieManager;
import com.quitto.server.domain.valueobject.CookieDomain;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final UserAuthenticationService service;
    private final CookieManager cookieManager;
    private final HttpCookieWriter cookieWriter;

    public AuthenticationController(UserAuthenticationService service, CookieManager cookieManager, HttpCookieWriter cookieWriter) {
        this.service = service;
        this.cookieManager = cookieManager;
        this.cookieWriter = cookieWriter;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginDTO data, HttpServletResponse response) {
        String token = service.login(data.name(), data.password());

        if (token.isBlank()){
            return ResponseEntity.status(401).build();
        }

        CookieDomain cookieDomain = cookieManager.createAccessTokenCookie(token);
        cookieWriter.writeCookie(response, cookieDomain);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO data, HttpServletResponse response) {

        if (data.password().length() <= 0 || data.password().length() >= 500) {
            return ResponseEntity.status(401).build();
        }

        String token = service.register(data.name(), data.password(), data.email());

        if (token.isBlank()){
            return ResponseEntity.status(401).build();
        }

        CookieDomain cookieDomain = cookieManager.createAccessTokenCookie(token);
        cookieWriter.writeCookie(response, cookieDomain);

        return ResponseEntity.ok().build();
    }

}
