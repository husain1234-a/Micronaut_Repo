package com.yash.usermanagementsystem.controller;

import com.yash.usermanagementsystem.dto.UserDTO;
import com.yash.usermanagementsystem.service.UserService;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import java.util.List;

@Controller("/api/api/users")
public class UserController {

    @Inject
    private UserService userService;

    // Admin endpoints
    @Post
    public UserDTO createUser(@Body @Valid UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @Put("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @Body @Valid UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }

    @Get
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @Get("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @Delete("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @Post("/{id}/notify")
    public void sendNotification(@PathVariable Long id, @Body String message) {
        userService.sendNotification(id, message);
    }

    @Post("/password-reset-requests/{requestId}/approve")
    public void approvePasswordReset(@PathVariable Long requestId) {
        userService.approvePasswordReset(requestId);
    }

    // User endpoints
    @Post("/login")
    public String login(@Body @Valid UserDTO loginDTO) {
        return userService.login(loginDTO);
    }

    @Post("/request-password-reset")
    public void requestPasswordReset(@Body String email) {
        userService.requestPasswordReset(email);
    }

    @Post("/reset-password")
    public void resetPassword(@Body @Valid UserDTO resetDTO) {
        userService.resetPassword(resetDTO);
    }

    @Put("/profile")
    public UserDTO updateProfile(@Body @Valid UserDTO userDTO) {
        return userService.updateCurrentUserProfile(userDTO);
    }
}