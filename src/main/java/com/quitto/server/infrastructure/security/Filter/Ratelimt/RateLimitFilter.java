package com.quitto.server.infrastructure.security.Filter.Ratelimt;

import java.io.IOException;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.quitto.server.domain.enums.RateLimitPolicy;
import com.quitto.server.infrastructure.interfaces.Ratelimit.RateLimit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@ConditionalOnProperty(
    prefix = "coffee.ratelimit",
    name = "enabled",
    havingValue = "true"
)
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimit rateLimiter;

    public RateLimitFilter(RateLimit rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws ServletException, IOException {
        try{
            String key = request.getRemoteAddr();

            if (key == null) {
                throw new IllegalArgumentException(
                    "Unable to apply rate limit: client IP address is missing"
            );
            }

            boolean allowed = rateLimiter.tryConsume(
                key,
                RateLimitPolicy.API);

                if (!allowed) {
                    response.setStatus(429);
                    logger.debug("Key: " + key + "cant acess API");
                return;
            }

        }catch(IllegalArgumentException IAE){

        }
        catch(Exception E){

        }
        filterChain.doFilter(request, response);
    }
}
