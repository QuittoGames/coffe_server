package com.quitto.server.infrastructure.services.Auth;

import java.util.Objects;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.quitto.server.domain.Repository.users.UserRepository;
import com.quitto.server.domain.exception.Auth.AuthenticationException;
import com.quitto.server.domain.interfaces.Auth.AuthenticationService;
import com.quitto.server.domain.interfaces.Auth.PasswordService;
import com.quitto.server.domain.models.User.User;
import com.quitto.server.domain.enums.Role;

@Service
public class SpringAuthenticationService implements AuthenticationService{
    private final AuthenticationManager authenticationManager;
    private final PasswordService passwordService;
    private final UserRepository userRepository;

    public SpringAuthenticationService(AuthenticationManager authenticationManager, UserRepository userRepository,PasswordService passwordService){
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    @Override
    public User authenticate(String username, String password )throws AuthenticationException{
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
            return userRepository.findByName(auth.getName()).orElseThrow();
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new AuthenticationException("Authentication failed", e);
        }
    }

    @Override
    public User register(String name, String password, String email){
        if (userRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User(
            name,
            passwordService.encode(password),
            email,
            Role.USER
        );

        return userRepository.save(user);
    }
}
