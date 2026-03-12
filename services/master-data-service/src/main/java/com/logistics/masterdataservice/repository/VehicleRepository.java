package com.logistics.masterdataservice.repository;

import com.logistics.masterdataservice.domain.Customer;
import com.logistics.masterdataservice.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID>, JpaSpecificationExecutor<Vehicle> {
    boolean existsByVehicleNumber(String vehicleNumber);
}
