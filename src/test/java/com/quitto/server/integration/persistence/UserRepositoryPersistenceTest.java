package com.quitto.server.integration.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.quitto.server.domain.Repository.users.UserRepository;
import com.quitto.server.domain.enums.Role;
import com.quitto.server.domain.models.User.User;

/**
 * Testes de persistência do {@link UserRepositoryAdapter} contra PostgreSQL
 * real (Testcontainers). Cobre save, busca por email/name, exists, findAll,
 * delete e as constraints de unicidade do banco.
 */
class UserRepositoryPersistenceTest extends PostgresPersistenceTestSupport {

    @Autowired
    private UserRepository userRepository;

    private User newUser(String name, String email) {
        return new User(name, "$2a$10$abc123hashabc123hashabc123hashabc", email, Role.USER);
    }

    @Test
    void save_persistsAndReturnsUserWithId() {
        User user = userRepository.save(newUser("quitto", "quitto@test.dev"));

        assertThat(user.getId()).isGreaterThan(0);
        assertThat(user.getName()).isEqualTo("quitto");
        assertThat(user.getEmail()).isEqualTo("quitto@test.dev");
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.getPasswordHash()).isNotBlank();
    }

    @Test
    void findById_returnsSavedUser() {
        User saved = userRepository.save(newUser("found", "found@test.dev"));

        Optional<User> result = userRepository.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("found@test.dev");
    }

    @Test
    void findById_missing_returnsEmpty() {
        assertThat(userRepository.findById(999_999L)).isEmpty();
    }

    @Test
    void findByEmail_returnsUser() {
        userRepository.save(newUser("by-email", "by-email@test.dev"));

        Optional<User> result = userRepository.findByEmail("by-email@test.dev");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("by-email");
    }

    @Test
    void findByEmail_missing_returnsEmpty() {
        assertThat(userRepository.findByEmail("nobody@test.dev")).isEmpty();
    }

    @Test
    void findByName_returnsUser() {
        userRepository.save(newUser("by-name", "by-name@test.dev"));

        Optional<User> result = userRepository.findByName("by-name");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("by-name@test.dev");
    }

    @Test
    void findByName_missing_returnsEmpty() {
        assertThat(userRepository.findByName("nobody")).isEmpty();
    }

    @Test
    void existsByEmail_and_existsByName_reflectDatabaseState() {
        userRepository.save(newUser("exists", "exists@test.dev"));

        assertThat(userRepository.existsByEmail("exists@test.dev")).isTrue();
        assertThat(userRepository.existsByEmail("missing@test.dev")).isFalse();
        assertThat(userRepository.existsByName("exists")).isTrue();
        assertThat(userRepository.existsByName("missing")).isFalse();
    }

    @Test
    void findAll_returnsAllSavedUsers() {
        userRepository.save(newUser("list-1", "list-1@test.dev"));
        userRepository.save(newUser("list-2", "list-2@test.dev"));

        List<User> all = userRepository.findAll();

        assertThat(all).hasSize(2);
    }

    @Test
    void deleteById_removesUser() {
        User saved = userRepository.save(newUser("to-delete", "to-delete@test.dev"));

        userRepository.deleteById(saved.getId());

        assertThat(userRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void save_duplicateEmail_violatesUniqueConstraint() {
        userRepository.save(newUser("first", "dup@test.dev"));

        assertThatThrownBy(() -> userRepository.save(newUser("second", "dup@test.dev")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void save_duplicateName_violatesUniqueConstraint() {
        userRepository.save(newUser("dup-name", "a@test.dev"));

        assertThatThrownBy(() -> userRepository.save(newUser("dup-name", "b@test.dev")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
