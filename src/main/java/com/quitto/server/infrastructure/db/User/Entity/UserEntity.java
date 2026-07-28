package com.quitto.server.infrastructure.db.User.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.quitto.server.domain.enums.Role;

@Entity
@Table(name = "\"user\"")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 150, unique = true)
    private String name;

    @Column(name = "password_hash", nullable = false, length = 150)
    private String passwordHash;

    @Column(name = "email", nullable = false, length = 150, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    private Role role = Role.USER;

    public UserEntity() {
    }

    public UserEntity(Long id, String name, String passwordHash, String email, Role role) {
        this.id = id;
        this.name = name;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserEntity [id=" + id + ", name=" + name + ", email=" + email + ", role=" + role + "]";
    }
}
