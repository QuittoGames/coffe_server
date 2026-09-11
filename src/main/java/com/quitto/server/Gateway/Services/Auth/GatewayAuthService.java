package com.quitto.server.Gateway.Services.Auth;

import org.springframework.stereotype.Service;

import com.quitto.server.domain.interfaces.Auth.AuthenticationService;
import com.quitto.server.Gateway.DTO.DataEntityUser;
import com.quitto.server.domain.interfaces.Token.TokenService;

import jakarta.validation.constraints.NotEmpty;

@Service
public class GatewayAuthService{
    private final AuthenticationService authenticationService;
    private final TokenService<Long> tokenService;

    public GatewayAuthService(AuthenticationService authenticationService, TokenService<Long> tokenService) {
        this.authenticationService = authenticationService;
        this.tokenService = tokenService;
    }

    public boolean IsAuthenticate(@NotEmpty DataEntityUser data_User){
        return tokenService.verifyToken(data_User.token());
    }
}
