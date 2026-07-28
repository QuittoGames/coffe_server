package com.quitto.server.infrastructure.services.Auth.Token.Jtw;

import java.util.Date;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import com.quitto.server.domain.interfaces.Token.TokenService;

@Service
public class JwtTokenService implements TokenService<Long> {

    @Value("${api.security.key}") // Get ENV value
    private String KEY;

    @Override
    public String generateToken(@NotNull Long id) throws IllegalArgumentException, JWTCreationException{
        Algorithm algorithm = Algorithm.HMAC256(this.KEY);

        String token = JWT.create()
            .withIssuer("coffe-api")
            .withSubject(String.valueOf(id))
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
            .sign(algorithm)
        ;

        return token;
    }

    @Override
    public boolean verifyToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(this.KEY);

            String JwtSubject = JWT.require(algorithm)
                .withIssuer("coffe-api")
                .build()
                .verify(token)
                .getSubject();


            return JwtSubject != null && !JwtSubject.isBlank();
        }catch(JWTVerificationException JTWVE){
            return false;
        }
    }

    @Override
    public Optional<Long> extractIdSubject(String token) throws JWTVerificationException{
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.KEY);

            String JwtSubject = JWT.require(algorithm)
                .withIssuer("coffe-api")
                .build()
                .verify(token)
                .getSubject();

            if (!JwtSubject.isBlank()){
                return Optional.of(Long.parseLong(JwtSubject));
            }

            return Optional.empty();
        } catch (JWTVerificationException e) {
            throw e;
        }
    }
}
