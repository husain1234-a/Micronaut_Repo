package com.yash.usermanagementsystem.service;

import com.yash.usermanagementsystem.dto.UserDTO;
import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(UUID id, UserDTO userDTO);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(UUID id);
    void deleteUser(UUID id);
    void sendNotification(UUID userId, String message);
    void approvePasswordReset(UUID requestId);
    String login(UserDTO loginDTO);
    void requestPasswordReset(String email);
    void resetPassword(UserDTO resetDTO);
    UserDTO getCurrentUserProfile();
    UserDTO updateCurrentUserProfile(UserDTO userDTO);
} 