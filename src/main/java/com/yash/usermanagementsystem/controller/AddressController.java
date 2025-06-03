package com.yash.usermanagementsystem.controller;

import com.yash.usermanagementsystem.dto.AddressDTO;
import com.yash.usermanagementsystem.service.AddressService;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller("/api/addresses")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class AddressController {

    @Inject
    private AddressService addressService;

    // Admin endpoints
    @Get
    @Secured({ "ADMIN" })
    public List<AddressDTO> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @Get("/{id}")
    @Secured({ "ADMIN" })
    public AddressDTO getAddressById(@PathVariable UUID id) {
        return addressService.getAddressById(id);
    }

    @Get("/user/{userId}")
    @Secured({ "ADMIN" })
    public List<AddressDTO> getAddressesByUserId(@PathVariable UUID userId) {
        return addressService.getAddressesByUserId(userId);
    }

    // User endpoints
    @Post
    public AddressDTO createAddress(@Body @Valid AddressDTO addressDTO) {
        return addressService.createAddress(addressDTO);
    }

    @Put("/{id}")
    public AddressDTO updateAddress(@PathVariable UUID id, @Body @Valid AddressDTO addressDTO) {
        return addressService.updateAddress(id, addressDTO);
    }

    @Delete("/{id}")
    public void deleteAddress(@PathVariable UUID id) {
        addressService.deleteAddress(id);
    }

    @Get("/my-addresses")
    public List<AddressDTO> getMyAddresses() {
        return addressService.getCurrentUserAddresses();
    }

    @Put("/{id}/set-primary")
    public AddressDTO setPrimaryAddress(@PathVariable UUID id) {
        return addressService.setPrimaryAddress(id);
    }
}