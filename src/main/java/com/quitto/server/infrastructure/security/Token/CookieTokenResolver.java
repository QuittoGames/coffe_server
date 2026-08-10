package com.quitto.server.infrastructure.security.Token;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.quitto.server.domain.interfaces.Token.TokenRequestContext;
import com.quitto.server.domain.interfaces.Token.TokenResolver;
import com.quitto.server.domain.valueobject.Cookie.CookieDomain;

@Component
public class CookieTokenResolver implements TokenResolver {

    @Override
    public Optional<String> resolve(TokenRequestContext request) {
        return request.getCookie("access_token")
            .map(CookieDomain::value);
    }
}
