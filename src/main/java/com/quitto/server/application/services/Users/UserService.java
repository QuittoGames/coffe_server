package com.quitto.server.application.services.Users;

import java.util.List;

import org.springframework.stereotype.Service;

import com.quitto.server.domain.Repository.users.UserRepository;
import com.quitto.server.domain.enums.Role;
import com.quitto.server.domain.exception.UserNotFoundException;
import com.quitto.server.domain.models.User.User;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository){
        this.repository = repository;
    }

    public List<User> listAll() {
        return repository.findAll();
    }

    public User getUser(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public User updateProfile(Long userId, String email) {
        User user = getUser(userId);
        user.setEmail(email);
        return repository.save(user);
    }

    public User updateRole(Long userId, Role role) {
        User user = getUser(userId);
        user.setRole(role);
        return repository.save(user);
    }

    public void deleteUser(Long userId) {
        getUser(userId); // garante existência; lança UserNotFoundException se não houver
        repository.deleteById(userId);
    }
}