package com.yash.usermanagementsystem.controller;

import com.yash.usermanagementsystem.dto.UserDTO;
import com.yash.usermanagementsystem.service.UserService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import java.util.List;

@Controller("/api/users")
@Tag(name = "User Management")
public class UserManagementSystemController {

    @Inject
    private UserService userService;

    @Post(consumes = MediaType.APPLICATION_JSON)
    public UserDTO createUser(@Body UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @Put("/{id}")
    public UserDTO updateUser(Long id, @Body UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }

    @Get
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @Get("/{id}")
    public UserDTO getUserById(Long id) {
        return userService.getUserById(id);
    }

    @Delete("/{id}")
    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }

    @Post("/login")
    public String login(@Body UserDTO loginDTO) {
        return userService.login(loginDTO);
    }

    @Post("/reset-password")
    public void resetPassword(@Body UserDTO resetDTO) {
        userService.resetPassword(resetDTO);
    }

    @Post("/request-reset")
    public void requestPasswordReset(@QueryValue String email) {
        userService.requestPasswordReset(email);
    }

    @Post("/{id}/notify")
    public void sendNotification(Long id, @QueryValue String message) {
        userService.sendNotification(id, message);
    }

    @Post("/{id}/approve-reset")
    public void approvePasswordReset(Long id) {
        userService.approvePasswordReset(id);
    }

    @Get("/profile")
    public UserDTO getCurrentUserProfile() {
        return userService.getCurrentUserProfile();
    }

    @Put("/profile")
    public UserDTO updateCurrentUserProfile(@Body UserDTO userDTO) {
        return userService.updateCurrentUserProfile(userDTO);
    }
} 