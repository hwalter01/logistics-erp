package com.logistics.masterdataservice.dto.request;

import com.logistics.masterdataservice.domain.enums.TrailerStatus;
import com.logistics.masterdataservice.domain.enums.TrailerType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request payload for creation or updating a trailer")
public record TrailerRequest(

        @Schema(description = "Unique trailer business number", example = "TRL-1001")
        @NotBlank String trailerNumber,

        @Schema(description = "Trailer license plate number", example = "HH-TR-1006")
        @NotBlank String licensePlate,

        @Schema(description = "Type of the trailer", example = "BOX")
        TrailerType trailerType,

        @Schema(description = "Current status of the trailer", example = "MAINTENANCE")
        @NotNull TrailerStatus status,

        @Schema(description = "Internal notes", example = "tires nearly depleted")
        String notes
) {
}
