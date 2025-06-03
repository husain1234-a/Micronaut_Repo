package com.yash.usermanagementsystem.dto;

import com.yash.usermanagementsystem.model.AddressType;
import io.micronaut.core.annotation.Nullable;
import jakarta.validation.constraints.*;
import java.util.UUID;

public class AddressDTO {
    private UUID id;

    @NotNull
    private UUID userId;

    @NotBlank
    @Size(max = 255)
    private String streetAddress;

    @NotBlank
    @Size(max = 100)
    private String city;

    @Nullable
    @Size(max = 100)
    private String state;

    @NotBlank
    private String postalCode;

    @NotBlank
    @Size(min = 2, max = 2)
    private String country;

    @Nullable
    private AddressType addressType;

    private boolean isPrimary = false;

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }
}