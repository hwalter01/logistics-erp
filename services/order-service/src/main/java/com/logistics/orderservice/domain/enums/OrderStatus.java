package com.logistics.orderservice.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Order Status")
public enum OrderStatus {
    @Schema(description = "Draft")
    DRAFT,

    @Schema(description = "Confirmed")
    CONFIRMED,

    @Schema(description = "Assigned to trip")
    ASSIGNED_TO_TRIP,

    @Schema(description = "Completed")
    COMPLETED,

    @Schema(description = "Cancelled")
    CANCELLED
}
