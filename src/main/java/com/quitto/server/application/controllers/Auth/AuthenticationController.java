package com.quitto.server.application.controllers.Auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quitto.server.application.dto.ErrorResponse;
import com.quitto.server.application.dto.Auth.LoginDTO;
import com.quitto.server.application.dto.Auth.RegisterDTO;
import com.quitto.server.application.interfaces.Cookies.HttpCookieWriter;
import com.quitto.server.application.services.Auth.UserAuthenticationService;
import com.quitto.server.application.services.Indepotecy.IdepotecyService;
import com.quitto.server.domain.interfaces.Cookies.CookieManager;
import com.quitto.server.domain.interfaces.OperationKey.OperationKey;
import com.quitto.server.domain.valueobject.Cookie.CookieDomain;

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
    private final IdepotecyService idempotencyService;

    public AuthenticationController(UserAuthenticationService service, CookieManager cookieManager, HttpCookieWriter cookieWriter, IdepotecyService idempotencyService) {
        this.service = service;
        this.cookieManager = cookieManager;
        this.cookieWriter = cookieWriter;
        this.idempotencyService = idempotencyService;
    }

    @PostMapping("/login")
    public ResponseEntity<ErrorResponse> login(@RequestBody @Valid LoginDTO data, HttpServletResponse response) {
        OperationKey key = data.idempotencyKey();

        if (key != null && idempotencyService.isDuplicate(key)) {
            return ResponseEntity.status(409).build();
        }

        String token = service.login(data.name(), data.password());

        if (token.isBlank()){
            return ResponseEntity.status(401).build();
        }

        CookieDomain cookieDomain = cookieManager.createAccessTokenCookie(token);
        cookieWriter.writeCookie(response, cookieDomain);

        if (key != null) {
            idempotencyService.markProcessed(key);
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO data, HttpServletResponse response) {
        OperationKey key = data.idempotencyKey();

        if (key != null && idempotencyService.isDuplicate(key)) {
            return ResponseEntity.status(409).build();
        }

        if (data.password().length() <= 0 || data.password().length() >= 500) {
            return ResponseEntity.status(401).build();
        }

        String token = service.register(data.name(), data.password(), data.email());

        if (token.isBlank()){
            return ResponseEntity.status(401).build();
        }

        CookieDomain cookieDomain = cookieManager.createAccessTokenCookie(token);
        cookieWriter.writeCookie(response, cookieDomain);

        if (key != null) {
            idempotencyService.markProcessed(key);
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        // Revoga o cookie HttpOnly: Max-Age=0 remove o cookie no browser.
        CookieDomain cookieDomain = cookieManager.createAccessTokenCookie("", 0);
        cookieWriter.writeCookie(response, cookieDomain);
        return ResponseEntity.ok().build();
    }

}
