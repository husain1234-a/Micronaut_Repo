package com.yash.usermanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.yash.usermanagement.model.User;

public interface UserService {
    User createUser(User user);

    List<User> getAllUsers();

    User getUserById(UUID id);

    User updateUser(UUID id, User user);

    void deleteUser(UUID id);

    Optional<User> findByEmail(String email);

    Optional<User> getUserByEmail(String email);

    boolean existsByEmail(String email);

    void changePassword(UUID userId, String newPassword);

    void requestPasswordChange(UUID userId);

    void approvePasswordChange(UUID userId);
}