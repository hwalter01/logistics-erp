package com.logistics.masterdataservice.repository;

import com.logistics.masterdataservice.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}