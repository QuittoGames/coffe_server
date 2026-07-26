package com.quitto.server.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.quitto.server.infrastructure.security.Filter.JwtAuthenticationFilter;
import com.quitto.server.infrastructure.services.OAuth.OAuth2UserProvisioningService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final OAuth2UserProvisioningService oauthService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(OAuth2UserProvisioningService oauthService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.oauthService = oauthService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
       http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/auth/login").permitAll()
            .requestMatchers("/auth/register").permitAll()
            .requestMatchers("/api/test").permitAll()
            .requestMatchers("/login").permitAll()
            .requestMatchers("/", "/css/**").permitAll()
            .requestMatchers("/mcp/**").hasAuthority("MCP")
            .requestMatchers("/admin/**").hasAuthority("ADMIN")
            // .requestMatchers("/api/**").hasAuthority("API")
            .anyRequest().authenticated()
        )
        .csrf(csrf -> csrf.disable())
        // .csrf(Customizer.withDefaults())

        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Object Filter before of class

        .exceptionHandling(ex -> ex
            .authenticationEntryPoint((req, res, authException) -> {
                res.setStatus(401);
            })
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
