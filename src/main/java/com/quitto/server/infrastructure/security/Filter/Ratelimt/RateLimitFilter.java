package com.quitto.server.infrastructure.security.Filter.Ratelimt;

import java.io.IOException;
import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity.AnonymousAuthenticationSpec;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.quitto.server.domain.enums.RateLimitPolicy;
import com.quitto.server.infrastructure.interfaces.Ratelimit.RateLimit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@ConditionalOnProperty(prefix = "coffee.ratelimit", name = "enabled", havingValue = "true")
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimit rateLimiter;

    public RateLimitFilter(RateLimit rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException {
        try {

            Optional<Authentication> authOptional = Optional.of(SecurityContextHolder.getContext().getAuthentication());

            String key = "";
            if (key == null || authOptional == null || (authOptional.isEmpty() || !authOptional.isPresent()))  {
                throw new IllegalArgumentException(
                        "Unable to apply rate limit: client IP address is missing");
            }

            Authentication auth = authOptional.get();

            if (auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)){
                key = auth.getName().trim();
            }else{
                key = request.getRemoteAddr();
            }

            logger.debug("RateLimit begin {}" + key);
            boolean allowed = rateLimiter.tryConsume(key, RateLimitPolicy.LOGIN);
            logger.debug("Allowed = {}" + String.valueOf(allowed));
            if (!allowed) {
                response.setStatus(429);
                logger.debug("Key: " + key + "cant acess API");
                return;
            }

        } catch (IllegalArgumentException e) {

            logger.warn("Invalid rate limit key", e);

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getMessage());
            return;

        } catch (Exception e) {

            logger.error("Rate limiter failure", e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Rate limiter failure");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
