package com.yash.usermanagementsystem.service.impl;

import com.yash.usermanagementsystem.dto.AddressDTO;
import com.yash.usermanagementsystem.model.Address;
import com.yash.usermanagementsystem.repository.AddressRepository;
import com.yash.usermanagementsystem.service.AddressService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class AddressServiceImpl implements AddressService {

    @Inject
    private AddressRepository addressRepository;

    @Override
    @Transactional
    public AddressDTO createAddress(AddressDTO addressDTO) {
        Address address = new Address();
        address.setUserId(addressDTO.getUserId());
        address.setStreetAddress(addressDTO.getStreetAddress());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPostalCode(addressDTO.getPostalCode());
        address.setCountry(addressDTO.getCountry());
        address.setAddressType(addressDTO.getAddressType());
        address.setPrimary(addressDTO.isPrimary());

        address = addressRepository.save(address);
        return convertToDTO(address);
    }

    @Override
    @Transactional
    public AddressDTO updateAddress(UUID id, AddressDTO addressDTO) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        address.setStreetAddress(addressDTO.getStreetAddress());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPostalCode(addressDTO.getPostalCode());
        address.setCountry(addressDTO.getCountry());
        address.setAddressType(addressDTO.getAddressType());
        address.setPrimary(addressDTO.isPrimary());

        address = addressRepository.save(address);
        return convertToDTO(address);
    }

    @Override
    @Transactional
    public void deleteAddress(UUID id) {
        addressRepository.deleteById(id);
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        return addressRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDTO getAddressById(UUID id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        return convertToDTO(address);
    }

    @Override
    public List<AddressDTO> getAddressesByUserId(UUID userId) {
        return addressRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AddressDTO> getCurrentUserAddresses() {
        // Implementation would get current user from security context
        return null;
    }

    @Override
    @Transactional
    public AddressDTO setPrimaryAddress(UUID id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        // Set all other addresses of the user as non-primary
        addressRepository.findByUserId(address.getUserId())
                .forEach(addr -> {
                    addr.setPrimary(false);
                    addressRepository.save(addr);
                });

        // Set this address as primary
        address.setPrimary(true);
        address = addressRepository.save(address);
        return convertToDTO(address);
    }

    private AddressDTO convertToDTO(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setUserId(address.getUserId());
        dto.setStreetAddress(address.getStreetAddress());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setPostalCode(address.getPostalCode());
        dto.setCountry(address.getCountry());
        dto.setAddressType(address.getAddressType());
        dto.setPrimary(address.isPrimary());
        return dto;
    }
}