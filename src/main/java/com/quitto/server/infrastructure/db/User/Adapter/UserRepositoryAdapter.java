package com.quitto.server.infrastructure.db.User.Adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.quitto.server.domain.Repository.users.UserRepository;
import com.quitto.server.domain.models.User.User;
import com.quitto.server.infrastructure.db.User.Entity.UserEntity;
import com.quitto.server.infrastructure.db.User.Mapper.UserMapper;
import com.quitto.server.infrastructure.db.User.Repository.JpaUserRepository;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    @Autowired
    private JpaUserRepository repository;

    @Override
    public User save(User user) {
        var entity = UserMapper.toInfra(user);
        UserEntity saved = repository.save(entity);
        return UserMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> userEntity = repository.findByEmail(email);

        if (userEntity.isEmpty()) {

            return Optional.empty();
        }

        return Optional.of(UserMapper.toDomain(userEntity.get()));
    }

    @Override
    public Optional<User> findByName(String name) {
        Optional<UserEntity> userEntity = repository.findByName(name);

        if (userEntity.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(UserMapper.toDomain(userEntity.get()));
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public Optional<User> findById(Long id){
        Optional<UserEntity> userEntity = repository.findById(id);
        if (userEntity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(UserMapper.toDomain(userEntity.get()));
    }

    @Override
    public List<User> findAll(){
        return repository.findAll().stream()
                .map(UserMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id){
        repository.deleteById(id);
    }
}
