package com.quitto.server.infrastructure.services.OAuth;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.quitto.server.domain.enums.Role;
import com.quitto.server.infrastructure.db.User.Entity.UserEntity;
import com.quitto.server.infrastructure.db.User.Repository.JpaUserRepository;

@Service
public class OAuth2UserProvisioningService extends DefaultOAuth2UserService {

    private final JpaUserRepository repo;

    public OAuth2UserProvisioningService(JpaUserRepository repo) {
        this.repo = repo;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {

        OAuth2User oauthUser = super.loadUser(request);

        String email = oauthUser.getAttribute("email");

        UserEntity user = repo.findByEmail(email)
            .orElseGet(() -> {
                UserEntity u = new UserEntity();
                u.setName(oauthUser.getAttribute("name"));
                u.setEmail(email);
                u.setPasswordHash(java.util.UUID.randomUUID().toString());
                u.setRole(Role.USER);
                return repo.save(u);
            });

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_"+ user.getRole().name()));

        return new DefaultOAuth2User(
            authorities,
            oauthUser.getAttributes(),
            "email"
        );
    }
}
