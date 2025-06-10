package com.yash.usermanagement.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;

import com.yash.usermanagement.model.Address;
import com.yash.usermanagement.service.AddressService;

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
        return HttpResponse.created(addressService.createAddress(address));
    }

    @Get("/{id}")
    @Operation(summary = "Get address by ID")
    public HttpResponse<Address> getAddressById(@PathVariable UUID id) {
        return addressService.getAddressById(id)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.notFound());
    }

    @Put("/{id}")
    @Operation(summary = "Update address by ID")
    public HttpResponse<Address> updateAddress(@PathVariable UUID id, @Body @Valid Address address) {
        return HttpResponse.ok(addressService.updateAddress(id, address));
    }

    @Delete("/{id}")
    @Operation(summary = "Delete address by ID")
    public HttpResponse<Void> deleteAddress(@PathVariable UUID id) {
        addressService.deleteAddress(id);
        return HttpResponse.noContent();
    }
}