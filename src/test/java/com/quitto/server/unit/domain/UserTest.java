package com.quitto.server.unit.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.quitto.server.domain.enums.Role;
import com.quitto.server.domain.models.User.User;
import com.quitto.server.domain.exception.Auth.InvalidPasswordException;

class UserTest {

    @Test
    void createsUserWithFullConstructor() {
        User user = new User(1L, "quitto", "hash123", "quitto@email.com", Role.ADMIN);
        assertEquals(1L, user.getId());
        assertEquals("quitto", user.getName());
        assertEquals("hash123", user.getPasswordHash());
        assertEquals("quitto@email.com", user.getEmail());
        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    void createsUserWithPartialConstructor() {
        User user = new User("novo", "hash456", "novo@email.com", Role.USER);
        assertEquals("novo", user.getName());
        assertEquals(Role.USER, user.getRole());
    }

    @Test
    void createsUserWithNoArgsConstructor() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    void equalsByEmail() {
        User user1 = new User(1L, "a", "h1", "same@email.com", Role.USER);
        User user2 = new User(2L, "b", "h2", "same@email.com", Role.USER);
        assertEquals(user1, user2);
    }

    @Test
    void notEqualsWhenDifferentEmail() {
        User user1 = new User(1L, "a", "h1", "a@email.com", Role.USER);
        User user2 = new User(2L, "b", "h2", "b@email.com", Role.USER);
        assertNotEquals(user1, user2);
    }

    @Test
    void notEqualsWhenNullEmail() {
        User user1 = new User(1L, "a", "h1", null, Role.USER);
        User user2 = new User(2L, "b", "h2", "b@email.com", Role.USER);
        assertNotEquals(user1, user2);
    }

    @Test
    void notEqualsAgainstDifferentClass() {
        User user = new User(1L, "a", "h1", "a@email.com", Role.USER);
        assertNotEquals(user, "not-a-user");
    }

    @Test
    void equalsAgainstSameReference() {
        User user = new User(1L, "a", "h1", "a@email.com", Role.USER);
        assertEquals(user, user);
    }

    @Test
    void changePasswordUpdatesHash() {
        User user = new User("quitto", "old-hash", "q@email.com", Role.USER);
        user.changePassword("new-hash");
        assertEquals("new-hash", user.getPasswordHash());
    }

    @Test
    void setEmailUpdatesEmail() {
        User user = new User("quitto", "hash", "old@email.com", Role.USER);
        user.setEmail("new@email.com");
        assertEquals("new@email.com", user.getEmail());
    }

    @Test
    void setRoleUpdatesRole() {
        User user = new User("quitto", "hash", "q@email.com", Role.USER);
        user.setRole(Role.ADMIN);
        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    void toStringContainsFields() {
        User user = new User(1L, "quitto", "hash", "q@email.com", Role.USER);
        String str = user.toString();
        assertTrue(str.contains("quitto"));
        assertTrue(str.contains("q@email.com"));
    }
}
