package com.logistics.masterdataservice.repository;

import com.logistics.masterdataservice.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID>, JpaSpecificationExecutor<Customer> {
    boolean existsByCustomerNumber(String customerNumber);
    boolean existsByShortCode(String shortCode);
}