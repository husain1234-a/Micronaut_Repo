package com.yash.usermanagement.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yash.usermanagement.model.User;
import com.yash.usermanagement.service.UserService;
import com.yash.usermanagement.exception.ResourceNotFoundException;
import com.yash.usermanagement.exception.ValidationException;
import com.yash.usermanagement.exception.DuplicateResourceException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller("/api/users")
@Tag(name = "User Management")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Post
    @Operation(summary = "Create a new user")
    public HttpResponse<User> createUser(@Body @Valid User user) {
        LOG.info("Creating new user");
        try {
            User createdUser = userService.createUser(user);
            return HttpResponse.created(createdUser);
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
    public HttpResponse<List<User>> getAllUsers() {
        LOG.info("Fetching all users");
        List<User> users = userService.getAllUsers();
        return HttpResponse.ok(users);
    }

    @Get("/{id}")
    @Operation(summary = "Get user by ID")
    public HttpResponse<User> getUserById(@PathVariable UUID id) {
        LOG.info("Fetching user with id: {}", id);
        try {
            User user = userService.getUserById(id);
            return HttpResponse.ok(user);
        } catch (ResourceNotFoundException e) {
            LOG.warn("User not found with id: {}", id);
            throw e;
        }
    }

    @Put("/{id}")
    @Operation(summary = "Update user")
    public HttpResponse<User> updateUser(@PathVariable UUID id, @Body @Valid User user) {
        LOG.info("Updating user with id: {}", id);
        try {
            User updatedUser = userService.updateUser(id, user);
            return HttpResponse.ok(updatedUser);
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
    public HttpResponse<User> getUserByEmail(@PathVariable String email) {
        LOG.info("Finding user by email: {}", email);
        try {
            return userService.findByEmail(email)
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
}