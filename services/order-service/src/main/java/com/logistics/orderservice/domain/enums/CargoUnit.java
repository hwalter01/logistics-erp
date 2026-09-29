package com.logistics.orderservice.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Units of Measure for cargo")
public enum CargoUnit {

    //Count and packaging
    @Schema(description = "Piece")
    PIECE,
    @Schema(description = "Pallet")
    PALLET,
    @Schema(description = "Package")
    PACKAGE,
    @Schema(description = "Box")
    BOX,
    //Weight
    @Schema(description = "Kilogram")
    KILOGRAM,
    @Schema(description = "Tonne")
    TONNE,
    //Volume
    @Schema(description = "Liter")
    LITER,
    @Schema(description = "Cubic Meter")
    CUBIC_METER,
    //Length
    @Schema(description = "Meter")
    METER,
    @Schema(description = "Loading Meter")
    LOADING_METER
}
