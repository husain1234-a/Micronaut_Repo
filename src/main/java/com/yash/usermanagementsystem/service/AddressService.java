package com.yash.usermanagementsystem.service;

import com.yash.usermanagementsystem.dto.AddressDTO;
import java.util.List;
import java.util.UUID;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO);

    AddressDTO updateAddress(UUID id, AddressDTO addressDTO);

    void deleteAddress(UUID id);

    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressById(UUID id);

    List<AddressDTO> getAddressesByUserId(UUID userId);

    List<AddressDTO> getCurrentUserAddresses();

    AddressDTO setPrimaryAddress(UUID id);
}