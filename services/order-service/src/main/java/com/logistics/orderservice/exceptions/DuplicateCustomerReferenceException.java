package com.logistics.orderservice.exceptions;

import java.util.UUID;

public class DuplicateCustomerReferenceException extends RuntimeException {

    public DuplicateCustomerReferenceException(
            UUID customerId,
            String customerReference
    ) {
        super(
                "Customer reference '" + customerReference
                        + "' already exists for customer "
                        + customerId
        );
    }
}