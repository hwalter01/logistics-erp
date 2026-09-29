package com.logistics.orderservice.repository;

import com.logistics.orderservice.domain.TransportOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransportOrderRepository
        extends JpaRepository<TransportOrder, UUID> {

    boolean existsByOrderNumber(String orderNumber);

    boolean existsByCustomerIdAndCustomerReference(
            UUID customerId,
            String customerReference
    );

    Optional<TransportOrder> findByOrderNumber(String orderNumber);
}
