package com.yash.usermanagementsystem.controller;

import com.yash.usermanagementsystem.dto.AddressDTO;
import com.yash.usermanagementsystem.service.AddressService;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller("/api/addresses")
@Tag(name = "Address Management")
public class AddressController {

    @Inject
    private AddressService addressService;

    // Admin endpoints
    @Get
    @Operation(summary = "Get all addresses")
    @ApiResponse(responseCode = "200", description = "List of addresses retrieved successfully")
    public List<AddressDTO> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @Get("/{id}")
    @Operation(summary = "Get address by ID")
    @ApiResponse(responseCode = "200", description = "Address retrieved successfully")
    public AddressDTO getAddressById(@Parameter(description = "Address ID") @PathVariable UUID id) {
        return addressService.getAddressById(id);
    }

    @Get("/user/{userId}")
    @Operation(summary = "Get addresses by user ID")
    @ApiResponse(responseCode = "200", description = "List of addresses retrieved successfully")
    public List<AddressDTO> getAddressesByUserId(@Parameter(description = "User ID") @PathVariable UUID userId) {
        return addressService.getAddressesByUserId(userId);
    }

    // User endpoints
    @Post
    @Operation(summary = "Create new address")
    @ApiResponse(responseCode = "200", description = "Address created successfully")
    public AddressDTO createAddress(@Body @Valid AddressDTO addressDTO) {
        return addressService.createAddress(addressDTO);
    }

    @Put("/{id}")
    @Operation(summary = "Update address")
    @ApiResponse(responseCode = "200", description = "Address updated successfully")
    public AddressDTO updateAddress(@Parameter(description = "Address ID") @PathVariable UUID id, @Body @Valid AddressDTO addressDTO) {
        return addressService.updateAddress(id, addressDTO);
    }

    @Delete("/{id}")
    @Operation(summary = "Delete address")
    @ApiResponse(responseCode = "204", description = "Address deleted successfully")
    public void deleteAddress(@Parameter(description = "Address ID") @PathVariable UUID id) {
        addressService.deleteAddress(id);
    }

    @Get("/my-addresses")
    @Operation(summary = "Get my addresses")
    @ApiResponse(responseCode = "200", description = "List of addresses retrieved successfully")
    public List<AddressDTO> getMyAddresses() {
        return addressService.getCurrentUserAddresses();
    }

    @Put("/{id}/set-primary")
    @Operation(summary = "Set primary address")
    @ApiResponse(responseCode = "200", description = "Primary address set successfully")
    public AddressDTO setPrimaryAddress(@Parameter(description = "Address ID") @PathVariable UUID id) {
        return addressService.setPrimaryAddress(id);
    }
}