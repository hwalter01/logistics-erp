package com.logistics.orderservice.exceptions;

import java.util.UUID;

public class MasterDataNotFoundException extends RuntimeException {

    public MasterDataNotFoundException(String resourceName, UUID id) {
        super(resourceName + " not found: " + id);
    }
}