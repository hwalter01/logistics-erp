package com.logistics.orderservice.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Stop Type")
public enum StopType {

    @Schema(description = "Pickup")
    PICKUP,

    @Schema(description = "Delivery")
    DELIVERY
}
