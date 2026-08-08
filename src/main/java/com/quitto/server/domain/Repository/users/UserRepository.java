package com.quitto.server.domain.Repository.users;

import java.util.List;
import java.util.Optional;
import com.quitto.server.domain.models.User.User;

/**
 * Porta de persistência do agregado {@link User}. Implementada na infraestrutura
 * (adapter JPA: {@code UserRepositoryAdapter} + {@code JpaUserRepository}).
 * Buscas individuais retornam {@link Optional} — nunca {@code null}.
 */
public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    List<User> findAll();

    boolean existsByEmail(String email);

    boolean existsByName(String name);

    void deleteById(Long id);
}
