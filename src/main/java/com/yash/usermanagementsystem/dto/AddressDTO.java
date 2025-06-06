package com.yash.usermanagementsystem.dto;
import io.micronaut.core.annotation.Introspected;
import com.yash.usermanagementsystem.model.AddressType;
import io.micronaut.core.annotation.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.util.UUID;

@Introspected
@Schema(description = "Address data transfer object")
public class AddressDTO {
    @Schema(description = "Unique identifier of the address")
    private UUID id;

    @NotNull
    @Schema(description = "ID of the user who owns this address", required = true)
    private UUID userId;
    
    @NotBlank(message = "Street address is required")
    @Size(max = 200, message = "Street address must be less than 200 characters")
    @Schema(description = "Street address", required = true)
    private String streetAddress;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City name must be less than 100 characters")
    @Schema(description = "City name", required = true)
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 100, message = "State name must be less than 100 characters")
    @Schema(description = "State or province", required = true)
    private String state;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country name must be less than 100 characters")
    @Schema(description = "Country code (2 letters)", required = true)
    private String country;

    @NotBlank(message = "Postal code is required")
    @Size(max = 20, message = "Postal code must be less than 20 characters")
    @Schema(description = "Postal or ZIP code", required = true)
    private String postalCode;
    
    @Nullable
    @Schema(description = "Type of address")
    private AddressType addressType;

    @Schema(description = "Whether this is the primary address", defaultValue = "false")
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