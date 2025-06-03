package com.yash.usermanagementsystem.controller;

import com.yash.usermanagementsystem.dto.UserDTO;
import com.yash.usermanagementsystem.service.UserService;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller("/api/users")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class UserController {

    @Inject
    private UserService userService;

    // Admin endpoints
    @Post
    @Secured({ "ADMIN" })
    public UserDTO createUser(@Body @Valid UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @Put("/{id}")
    @Secured({ "ADMIN" })
    public UserDTO updateUser(@PathVariable UUID id, @Body @Valid UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }

    @Get
    @Secured({ "ADMIN" })
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @Get("/{id}")
    @Secured({ "ADMIN" })
    public UserDTO getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @Delete("/{id}")
    @Secured({ "ADMIN" })
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    @Post("/{id}/notify")
    @Secured({ "ADMIN" })
    public void sendNotification(@PathVariable UUID id, @Body String message) {
        userService.sendNotification(id, message);
    }

    @Post("/password-reset-requests/{requestId}/approve")
    @Secured({ "ADMIN" })
    public void approvePasswordReset(@PathVariable UUID requestId) {
        userService.approvePasswordReset(requestId);
    }

    // User endpoints
    @Post("/login")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public String login(@Body @Valid UserDTO loginDTO) {
        return userService.login(loginDTO);
    }

    @Post("/request-password-reset")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public void requestPasswordReset(@Body String email) {
        userService.requestPasswordReset(email);
    }

    @Post("/reset-password")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public void resetPassword(@Body @Valid UserDTO resetDTO) {
        userService.resetPassword(resetDTO);
    }

    @Get("/profile")
    public UserDTO getProfile() {
        return userService.getCurrentUserProfile();
    }

    @Put("/profile")
    public UserDTO updateProfile(@Body @Valid UserDTO userDTO) {
        return userService.updateCurrentUserProfile(userDTO);
    }
}