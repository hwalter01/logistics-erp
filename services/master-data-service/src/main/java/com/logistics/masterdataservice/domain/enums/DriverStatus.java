package com.logistics.masterdataservice.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status of the driver")
public enum DriverStatus {

    @Schema(description = "Active")
    ACTIVE,

    @Schema(description = "Inactive")
    INACTIVE,

    @Schema(description = "On leave")
    ON_LEAVE,

    @Schema(description = "Sick")
    SICK,

    @Schema(description = "Suspended")
    SUSPENDED
}
