package com.quitto.server.infrastructure.security.Filter;

import com.quitto.server.domain.exception.InvalidTokenException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.quitto.server.domain.Repository.users.UserRepository;
import com.quitto.server.domain.interfaces.Token.TokenRequestContext;
import com.quitto.server.domain.interfaces.Token.TokenService;
import com.quitto.server.domain.models.User.User;
import com.quitto.server.infrastructure.security.SecurityUser;
import com.quitto.server.infrastructure.security.Filter.Adpter.HttpTokenRequestContext;
import com.quitto.server.infrastructure.services.Auth.Token.TokenResolverManager;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private final TokenService<Long> tokenService;
    private final TokenResolverManager manager;
    private final UserRepository repository;

    public JwtAuthenticationFilter(TokenService<Long> tokenService,UserRepository repository,TokenResolverManager manager) {
        this.tokenService = tokenService;
        this.repository = repository;
        this.manager = manager;
    }

    // Validate the JWT sent in the Authorization header
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException{
        try{
            Optional<String> token = recoverToken(request); // The token cannot be null because recoverToken() validates it before.

            String jwt = token
                .filter(t -> !t.isBlank())
                .orElse(null);

           if (jwt == null){
                filterChain.doFilter(request, response);
                return;
            }

            boolean isValidToken = tokenService.verifyToken(jwt);

            if(!isValidToken) {
                throw new InvalidTokenException("The provided JWT token is invalid or expired");
            }

            Long id = tokenService.extractIdSubject(jwt)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token subject"));

            User user_domain = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

            var user = new SecurityUser(id, user_domain.getName(), user_domain.getRole());

            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.role().name()));

            var auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        catch (IllegalArgumentException e) {
            logger.warn("Authentication failed due to invalid authentication data: {}", e);
            SecurityContextHolder.clearContext();
        }
        catch(JWTVerificationException e){
            logger.error("JWT verification failed", e);
            SecurityContextHolder.clearContext();
        }
        catch(InvalidTokenException e){
            logger.warn("Authentication argument error", e);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    public Optional<String> recoverToken(HttpServletRequest request){
        TokenRequestContext context = new HttpTokenRequestContext(request);
        return manager.resolve(context); //Get all tokens in session
    }
}
