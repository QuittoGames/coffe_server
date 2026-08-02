package com.quitto.server.infrastructure.security;

import com.quitto.server.domain.enums.Role;

public record SecurityUser(
    Long id,
    String name,
    Role role
) {
    @Override
    public String toString() {
        return name;
    }
}
