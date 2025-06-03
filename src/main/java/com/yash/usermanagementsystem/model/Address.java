package com.yash.usermanagementsystem.model;

import io.micronaut.core.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.UUID;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Column(name = "user_id")
    private UUID userId;

    @NotBlank
    @Size(max = 255)
    @Column(name = "street_address")
    private String streetAddress;

    @NotBlank
    @Size(max = 100)
    private String city;

    @Nullable
    @Size(max = 100)
    private String state;

    @NotBlank
    @Column(name = "postal_code")
    private String postalCode;

    @NotBlank
    @Size(min = 2, max = 2)
    @Column(name = "country")
    private String country;

    @Nullable
    @Enumerated(EnumType.STRING)
    @Column(name = "address_type")
    private AddressType addressType;

    @Column(name = "is_primary")
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