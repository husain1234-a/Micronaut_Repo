package com.yash.usermanagement.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;

import com.yash.usermanagement.model.Address;
import com.yash.usermanagement.service.AddressService;
import com.yash.usermanagement.exception.ResourceNotFoundException;
import com.yash.usermanagement.exception.ValidationException;

@Controller("/api/addresses")
@Tag(name = "Address Management")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @Post
    @Operation(summary = "Create a new address")
    public HttpResponse<Address> createAddress(@Body @Valid Address address) {
        try {
            Address createdAddress = addressService.createAddress(address);
            return HttpResponse.created(createdAddress);
        } catch (ValidationException e) {
            throw e;
        }
    }

    @Get("/{id}")
    @Operation(summary = "Get address by ID")
    public HttpResponse<Address> getAddressById(@PathVariable UUID id) {
        try {
            return addressService.getAddressById(id)
                    .map(HttpResponse::ok)
                    .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
        } catch (ResourceNotFoundException e) {
            throw e;
        }
    }

    @Put("/{id}")
    @Operation(summary = "Update address by ID")
    public HttpResponse<Address> updateAddress(@PathVariable UUID id, @Body @Valid Address address) {
        try {
            Address updatedAddress = addressService.updateAddress(id, address);
            return HttpResponse.ok(updatedAddress);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (ValidationException e) {
            throw e;
        }
    }

    @Delete("/{id}")
    @Operation(summary = "Delete address by ID")
    public HttpResponse<Void> deleteAddress(@PathVariable UUID id) {
        try {
            addressService.deleteAddress(id);
            return HttpResponse.noContent();
        } catch (ResourceNotFoundException e) {
            throw e;
        }
    }
}