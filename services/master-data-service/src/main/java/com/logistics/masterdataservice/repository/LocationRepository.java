package com.logistics.masterdataservice.repository;

import com.logistics.masterdataservice.domain.Customer;
import com.logistics.masterdataservice.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID>, JpaSpecificationExecutor<Location> {
}
