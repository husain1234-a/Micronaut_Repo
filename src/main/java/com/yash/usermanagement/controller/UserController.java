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
        User createdUser = userService.createUser(user);
        return HttpResponse.created(createdUser);
    }

    @Get
    @Operation(summary = "Get all users")
    public HttpResponse<List<User>> getAllUsers() {
        LOG.info("Fetching all users");
        return HttpResponse.ok(userService.getAllUsers());
    }

    @Get("/{id}")
    @Operation(summary = "Get user by ID")
    public HttpResponse<User> getUserById(@PathVariable UUID id) {
        LOG.info("Fetching user with id: {}", id);
        User user = userService.getUserById(id);
        return HttpResponse.ok(user);
    }

    @Put("/{id}")
    @Operation(summary = "Update user")
    public HttpResponse<User> updateUser(@PathVariable UUID id, @Body @Valid User user) {
        LOG.info("Updating user with id: {}", id);
        User updatedUser = userService.updateUser(id, user);
        return HttpResponse.ok(updatedUser);
    }

    @Delete("/{id}")
    @Operation(summary = "Delete user")
    public HttpResponse<Void> deleteUser(@PathVariable UUID id) {
        LOG.info("Deleting user with id: {}", id);
        userService.deleteUser(id);
        return HttpResponse.noContent();
    }

    @Get("/email/{email}")
    @Operation(summary = "Get user by email")
    public HttpResponse<User> getUserByEmail(@PathVariable String email) {
        LOG.info("Finding user by email: {}", email);
        return userService.findByEmail(email)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.notFound());
    }

    @Post("/{id}/change-password")
    @Operation(summary = "Request password change")
    public HttpResponse<Void> requestPasswordChange(@PathVariable UUID id) {
        userService.requestPasswordChange(id);
        return HttpResponse.accepted();
    }

    @Put("/{id}/approve-password-change")
    @Operation(summary = "Approve password change request")
    public HttpResponse<Void> approvePasswordChange(@PathVariable UUID id) {
        userService.approvePasswordChange(id);
        return HttpResponse.ok();
    }
}