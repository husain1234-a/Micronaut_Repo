package com.yash.usermanagementsystem.service.impl;

import com.yash.usermanagementsystem.dto.UserDTO;
import com.yash.usermanagementsystem.model.User;
import com.yash.usermanagementsystem.repository.UserRepository;
import com.yash.usermanagementsystem.service.EmailService;
import com.yash.usermanagementsystem.service.UserService;
// import io.micronaut.security.authentication.Authentication;
// import io.micronaut.security.authentication.AuthenticationResponse;
// import io.micronaut.security.authentication.UsernamePasswordCredentials;
// import io.micronaut.security.token.jwt.generator.JwtTokenGenerator;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class UserServiceImpl implements UserService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private EmailService emailService;

    // @Inject
    // private JwtTokenGenerator tokenGenerator;

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword()); // Should be hashed in production
        user.setGender(userDTO.getGender());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setRole(userDTO.getRole());

        user = userRepository.save(user);
        return convertToDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updateUser(UUID id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setGender(userDTO.getGender());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setRole(userDTO.getRole());

        user = userRepository.save(user);
        return convertToDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public void sendNotification(UUID userId, String message) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            emailService.sendEmail(user.getEmail(), "Notification", message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send notification: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void approvePasswordReset(UUID requestId) {
        // Implementation for password reset approval
        // This would typically involve checking a password reset request table
        // and sending an email with a reset link
    }

    @Override
    public String login(UserDTO loginDTO) {
        try {
            User user = userRepository.findByEmail(loginDTO.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!user.getPassword().equals(loginDTO.getPassword())) { // Note: In production, use proper password hashing
                throw new RuntimeException("Invalid password");
            }

            // For testing, just return a simple success message
            return "Login successful for user: " + user.getEmail();

            // Commented out JWT token generation
            /*
            Map<String, Object> claims = new HashMap<>();
            claims.put("sub", user.getEmail());
            claims.put("roles", user.getRole());

            return tokenGenerator.generateToken(claims)
                    .orElseThrow(() -> new RuntimeException("Failed to generate token"));
            */
        } catch (Exception e) {
            throw new RuntimeException("Login failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void requestPasswordReset(String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Create password reset request and notify admin
            emailService.sendEmail("admin@example.com", "Password Reset Request",
                    "User " + user.getEmail() + " has requested a password reset.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to request password reset: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void resetPassword(UserDTO resetDTO) {
        User user = userRepository.findByEmail(resetDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(resetDTO.getPassword()); // Should be hashed in production
        userRepository.save(user);
    }

    @Override
    public UserDTO getCurrentUserProfile() {
        // Implementation would get current user from security context
        return null;
    }

    @Override
    @Transactional
    public UserDTO updateCurrentUserProfile(UserDTO userDTO) {
        // Implementation would get current user from security context and update
        return null;
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setGender(user.getGender());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        return dto;
    }
}