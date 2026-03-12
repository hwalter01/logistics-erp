package com.logistics.masterdataservice.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status of the vehicle")
public enum VehicleStatus {

    @Schema(description = "Vehicle is currently active")
    ACTIVE,

    @Schema(description = "Vehicle is currently inactive")
    INACTIVE,

    @Schema(description = "Vehicle is currently undergoing maintenance")
    MAINTENANCE
}
