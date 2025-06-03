package com.yash.usermanagementsystem.repository;

import com.yash.usermanagementsystem.model.Address;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
    List<Address> findByUserId(UUID userId);
    List<Address> findByUserIdAndIsPrimaryTrue(UUID userId);
} 