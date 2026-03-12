package com.logistics.masterdataservice.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.logistics.masterdataservice.domain.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID>, JpaSpecificationExecutor<Customer> {
    boolean existsByCustomerNumber(String customerNumber);

    Optional<Customer> findByCustomerNumber(String customerNumber);
}