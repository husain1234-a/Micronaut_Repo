package com.yash.usermanagementsystem.service;

import com.yash.usermanagementsystem.dto.UserDTO;
import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(Long id, UserDTO userDTO);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    void deleteUser(Long id);
    void sendNotification(Long userId, String message);
    void approvePasswordReset(Long requestId);
    String login(UserDTO loginDTO);
    void requestPasswordReset(String email);
    void resetPassword(UserDTO resetDTO);
    UserDTO getCurrentUserProfile();
    UserDTO updateCurrentUserProfile(UserDTO userDTO);
} 