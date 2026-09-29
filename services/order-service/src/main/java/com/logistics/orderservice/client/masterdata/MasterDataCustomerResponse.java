package com.logistics.orderservice.client.masterdata;

import java.util.UUID;

public record MasterDataCustomerResponse(
        UUID customerId,

        String shortCode
) {}
