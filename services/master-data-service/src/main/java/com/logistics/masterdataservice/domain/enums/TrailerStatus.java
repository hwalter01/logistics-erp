package com.logistics.masterdataservice.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status of the trailer")
public enum TrailerStatus {

    @Schema(description = "Active")
    ACTIVE,

    @Schema(description = "Inactive")
    INACTIVE,

    @Schema(description = "Undergoing Maintenance")
    MAINTENANCE
}
