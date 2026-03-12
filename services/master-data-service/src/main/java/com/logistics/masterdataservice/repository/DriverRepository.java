package com.logistics.masterdataservice.repository;

import com.logistics.masterdataservice.domain.Customer;
import com.logistics.masterdataservice.domain.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface DriverRepository extends JpaRepository<Driver, UUID>, JpaSpecificationExecutor<Driver> {
    boolean existsByDriverNumber(String driverNumber);
}
