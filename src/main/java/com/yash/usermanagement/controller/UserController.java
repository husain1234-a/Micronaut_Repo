package com.yash.usermanagement.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yash.usermanagement.dto.CreateUserRequest;
import com.yash.usermanagement.dto.UpdateUserRequest;
import com.yash.usermanagement.dto.UserResponse;
import com.yash.usermanagement.model.User;
import com.yash.usermanagement.service.UserService;
import com.yash.usermanagement.service.NotificationService;
import com.yash.usermanagement.exception.ResourceNotFoundException;
import com.yash.usermanagement.exception.ValidationException;
import com.yash.usermanagement.exception.DuplicateResourceException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller("/api/users")
@Tag(name = "User Management")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final NotificationService notificationService;

    public UserController(UserService userService, NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @Post
    @Operation(summary = "Create a new user")
    public HttpResponse<UserResponse> createUser(@Body @Valid CreateUserRequest request) {
        LOG.info("Creating new user");
        try {
            User user = convertToUser(request);
            User createdUser = userService.createUser(user);

            // Send welcome notification and email
            notificationService.sendUserCreationNotification(
                    createdUser.getId(),
                    createdUser.getEmail(),
                    request.getPassword());

            return HttpResponse.created(convertToUserResponse(createdUser));
        } catch (DuplicateResourceException e) {
            LOG.warn("Duplicate user creation attempted: {}", e.getMessage());
            throw e;
        } catch (ValidationException e) {
            LOG.warn("Invalid user data: {}", e.getMessage());
            throw e;
        }
    }

    @Get
    @Operation(summary = "Get all users")
    public HttpResponse<List<UserResponse>> getAllUsers() {
        LOG.info("Fetching all users");
        List<User> users = userService.getAllUsers();
        List<UserResponse> userResponses = users.stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
        return HttpResponse.ok(userResponses);
    }

    @Get("/{id}")
    @Operation(summary = "Get user by ID")
    public HttpResponse<UserResponse> getUserById(@PathVariable UUID id) {
        LOG.info("Fetching user with id: {}", id);
        try {
            User user = userService.getUserById(id);
            return HttpResponse.ok(convertToUserResponse(user));
        } catch (ResourceNotFoundException e) {
            LOG.warn("User not found with id: {}", id);
            throw e;
        }
    }

    @Put("/{id}")
    @Operation(summary = "Update user")
    public HttpResponse<UserResponse> updateUser(@PathVariable UUID id, @Body @Valid UpdateUserRequest request) {
        LOG.info("Updating user with id: {}", id);
        try {
            User user = convertToUser(request);
            User updatedUser = userService.updateUser(id, user);
            return HttpResponse.ok(convertToUserResponse(updatedUser));
        } catch (ResourceNotFoundException e) {
            LOG.warn("User not found for update with id: {}", id);
            throw e;
        } catch (ValidationException e) {
            LOG.warn("Invalid user data for update: {}", e.getMessage());
            throw e;
        }
    }

    @Delete("/{id}")
    @Operation(summary = "Delete user")
    public HttpResponse<Void> deleteUser(@PathVariable UUID id) {
        LOG.info("Deleting user with id: {}", id);
        try {
            userService.deleteUser(id);
            return HttpResponse.noContent();
        } catch (ResourceNotFoundException e) {
            LOG.warn("User not found for deletion with id: {}", id);
            throw e;
        }
    }

    @Get("/email/{email}")
    @Operation(summary = "Get user by email")
    public HttpResponse<UserResponse> getUserByEmail(@PathVariable String email) {
        LOG.info("Finding user by email: {}", email);
        try {
            return userService.findByEmail(email)
                    .map(this::convertToUserResponse)
                    .map(HttpResponse::ok)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        } catch (ValidationException e) {
            LOG.warn("Invalid email format: {}", email);
            throw e;
        }
    }

    @Post("/{id}/change-password")
    @Operation(summary = "Request password change")
    public HttpResponse<Void> requestPasswordChange(@PathVariable UUID id) {
        LOG.info("Requesting password change for user with id: {}", id);
        try {
            userService.requestPasswordChange(id);
            return HttpResponse.accepted();
        } catch (ResourceNotFoundException e) {
            LOG.warn("User not found for password change request with id: {}", id);
            throw e;
        }
    }

    @Put("/{id}/approve-password-change")
    @Operation(summary = "Approve password change request")
    public HttpResponse<Void> approvePasswordChange(@PathVariable UUID id) {
        LOG.info("Approving password change for user with id: {}", id);
        try {
            userService.approvePasswordChange(id);
            return HttpResponse.ok();
        } catch (ResourceNotFoundException e) {
            LOG.warn("User not found for password change approval with id: {}", id);
            throw e;
        }
    }

    // Helper methods for conversion
    private User convertToUser(CreateUserRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setGender(request.getGender());
        user.setRole(request.getRole());
        user.setAddress(request.getAddress());
        return user;
    }

    private User convertToUser(UpdateUserRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setGender(request.getGender());
        user.setRole(request.getRole());
        user.setAddress(request.getAddress());
        return user;
    }

    private UserResponse convertToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setDateOfBirth(user.getDateOfBirth());
        response.setAddress(user.getAddress());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setGender(user.getGender());
        response.setRole(user.getRole());
        return response;
    }
}