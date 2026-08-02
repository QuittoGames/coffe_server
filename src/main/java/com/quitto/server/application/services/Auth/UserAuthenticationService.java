package com.quitto.server.application.services.Auth;

import org.springframework.stereotype.Service;

import com.quitto.server.domain.exception.AuthenticationException;

import com.quitto.server.domain.interfaces.Auth.AuthenticationService;
import com.quitto.server.domain.interfaces.Token.TokenService;
import com.quitto.server.domain.models.User.User;

@Service
public class UserAuthenticationService {

    private final AuthenticationService authenticationService;
    private final TokenService<Long> tokenService;

    public UserAuthenticationService(AuthenticationService authenticationService, TokenService<Long> tokenService) {
        this.authenticationService = authenticationService;
        this.tokenService = tokenService;
    }

    public String login(String name, String password) throws AuthenticationException {
        User user = authenticationService.authenticate(name, password);
        return tokenService.generateToken(user.getId());
    }

    public String register(String name , String password , String email){
        User user = authenticationService.register(name, password, email);
        return tokenService.generateToken(user.getId());
    }
}
