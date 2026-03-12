package com.logistics.masterdataservice.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Type of the trailer")
public enum TrailerType {

    @Schema(description = "Courtainsider")
    CURTAINSIDER,

    @Schema(description = "Reefer")
    REEFER,

    @Schema(description = "Flatbed")
    FLATBED,

    @Schema(description = "Tank")
    TANK,

    @Schema(description = "Box")
    BOX
}
