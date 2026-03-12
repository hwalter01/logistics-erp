package com.logistics.masterdataservice.repository;

import com.logistics.masterdataservice.domain.Customer;
import com.logistics.masterdataservice.domain.Trailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface TrailerRepository extends JpaRepository<Trailer, UUID>, JpaSpecificationExecutor<Trailer> {
    boolean existsByTrailerNumber(String trailerNumber);
}
