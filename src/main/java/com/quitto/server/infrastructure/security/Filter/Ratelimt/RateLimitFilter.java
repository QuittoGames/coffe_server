package com.quitto.server.infrastructure.security.Filter.Ratelimt;

import java.io.IOException;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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

/**
 * Filter that enforces per-route rate limiting using {@link RateLimit}
 * (Bucket4j + Redis under the hood).
 *
 * <p>Routes are bucketed by URL prefix into {@link RateLimitPolicy} values:
 * anything under {@code /auth/} uses the {@code LOGIN} policy (login,
 * register, logout — same auth surface); every other route falls back to
 * {@code API}. The {@code UPLOAD} policy is reserved for explicit upload
 * endpoints (not yet wired).
 *
 * <p>The key is the authenticated user name when present, otherwise the
 * remote IP. This filter is only registered when
 * {@code coffee.ratelimit.enabled=true}.
 */
@Component
@ConditionalOnProperty(prefix = "coffee.ratelimit", name = "enabled", havingValue = "true")
public class RateLimitFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);

    /** Path prefix routed to the {@link RateLimitPolicy#LOGIN} bucket. */
    private static final String AUTH_PATH_PREFIX = "/auth/";

    private final RateLimit rateLimiter;

    public RateLimitFilter(RateLimit rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/app/")
            || path.startsWith("/css/")
            || path.startsWith("/js/")
            || path.startsWith("/webjars/")
            || path.startsWith("/favicon.ico")
            || path.startsWith("/error")
            || path.startsWith("/actuator/")
            // WebSocket handshakes — not API calls; must not burn the bucket
            // (endpoint may not even exist yet, so every attempt is a 404).
            || path.startsWith("/protocol")
            || path.equals("/ws");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain)
            throws ServletException, IOException {

        final String key = resolveKey(request);
        final RateLimitPolicy policy = resolvePolicy(request);
        final String uri = request.getRequestURI();
        boolean allowed;

        try {
            Objects.requireNonNull(key);
            Objects.requireNonNull(policy);
            Objects.requireNonNull(uri);

            log.debug("RateLimit begin key={} policy={} uri={}", key, policy, uri);

            allowed = rateLimiter.tryConsume(key, policy);

            log.debug("RateLimit allowed={} key={} policy={}", allowed, key, policy);

        } catch (IllegalArgumentException e) {
            log.warn("RateLimit invalid key key={} uri={}", key, uri, e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return;
        } catch (Exception e) {
            log.error("RateLimit failure key={} uri={}", key, uri, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Rate limiter failure");
            return;
        }

        if (!allowed) {
            log.warn("RateLimit denied (429) key={} policy={} uri={}", key, policy, uri);
            response.setStatus(429);
            return;
        }


        filterChain.doFilter(request, response);
    }

    /**
     * Resolve the rate-limit bucket key for the current request.
     *
     * <p>Uses the authenticated principal's name when available so that the
     * same user shares a bucket across IPs (e.g. behind a proxy or NAT).
     * Falls back to the remote address for anonymous traffic.
     */
    private String resolveKey(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken)) {
            return auth.getName().trim();
        }
        return request.getRemoteAddr();
    }

    /**
     * Resolve the {@link RateLimitPolicy} for the current request.
     *
     * <p>Everything under {@code /auth/} shares the {@code LOGIN} bucket;
     * every other route uses {@code API}.
     */
    private RateLimitPolicy resolvePolicy(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (path != null && path.startsWith(AUTH_PATH_PREFIX)) {
            return RateLimitPolicy.LOGIN;
        }
        return RateLimitPolicy.API;
    }
}
