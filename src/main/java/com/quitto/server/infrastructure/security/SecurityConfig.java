package com.quitto.server.infrastructure.security;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.quitto.server.infrastructure.security.Filter.JwtAuthenticationFilter;
import com.quitto.server.infrastructure.security.Filter.Ratelimt.RateLimitFilter;
import com.quitto.server.infrastructure.services.OAuth.OAuth2UserProvisioningService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final OAuth2UserProvisioningService oauthService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RateLimitFilter rateLimitFilter;

    public SecurityConfig(OAuth2UserProvisioningService oauthService,
                          JwtAuthenticationFilter jwtAuthenticationFilter,
                          ObjectProvider<RateLimitFilter> rateLimitFilterProvider) {
        this.oauthService = oauthService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        // RateLimitFilter only exists when coffee.ratelimit.enabled=true
        // (Bucket4jConfig is conditional on the same property).
        this.rateLimitFilter = rateLimitFilterProvider.getIfAvailable();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
       http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/auth/login").permitAll()
            .requestMatchers("/auth/register").permitAll()
            .requestMatchers("/login").permitAll()
            .requestMatchers("/error").permitAll()
            .requestMatchers("/", "/css/**").permitAll()
            .requestMatchers("/app", "/app/", "/app/**").permitAll()
            .requestMatchers("/mcp/**").hasAuthority("MCP")
            .requestMatchers("/admin/**").hasAuthority("ADMIN")
            // .requestMatchers("/api/**").hasAuthority("API")
            .anyRequest().authenticated()
        )
        .csrf(csrf -> csrf.disable())
        // .csrf(Customizer.withDefaults())

        // Filter
        // Request
        //    |
        //    v
        // JwtAuthenticationFilter
        //    |
        //    v
        // RateLimitFilter
        //    |
        //    v
        // Controller

        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Object Filter before of class

        if (rateLimitFilter != null) {
            http.addFilterAfter(rateLimitFilter, JwtAuthenticationFilter.class); // Object Filter before of class
        }

        http.exceptionHandling(ex -> ex
            .authenticationEntryPoint((req, res, authException) -> {
                res.setStatus(401);
            })
        )

        // HSTS
        .headers(header -> header
            .httpStrictTransportSecurity(hsts -> hsts
                .includeSubDomains(true)
                .maxAgeInSeconds(31536000)
            )
        )

        .oauth2Login(oauth -> oauth
            .userInfoEndpoint(userInfo ->
                userInfo.userService(oauthService)
            )
        );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
