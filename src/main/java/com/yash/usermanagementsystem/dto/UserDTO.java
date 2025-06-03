package com.yash.usermanagementsystem.dto;

import com.yash.usermanagementsystem.model.Gender;
import io.micronaut.core.annotation.Nullable;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;

public class UserDTO {
    private UUID id;

    @NotBlank
    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "First name should not contain special characters")
    private String firstName;

    @Nullable
    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Last name should not contain special characters")
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z]).*$", message = "Password must contain at least one number and one uppercase letter")
    private String password;

    @Nullable
    private Gender gender;

    @Nullable
    @Past
    private LocalDate dateOfBirth;

    @Nullable
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Phone number must be in E.164 format")
    private String phoneNumber;

    @Nullable
    private UUID addressId;

    private String role = "USER";

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UUID getAddressId() {
        return addressId;
    }

    public void setAddressId(UUID addressId) {
        this.addressId = addressId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}