package com.logistics.masterdataservice.dto.response;

import com.logistics.masterdataservice.domain.enums.TrailerStatus;
import com.logistics.masterdataservice.domain.enums.TrailerType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;
@Schema(description = "Request payload for creating or updating a trailer")
public record TrailerResponse(

        @Schema(description = "Unique identifier of the trailer", example = "041c2882-f401-43be-81a0-398fedc08dee")
        UUID trailerId,



        @Schema(description = "Unique trailer business number", example = "TRL-1001")
        String trailerNumber,

        @Schema(description = "Trailer license plate number", example = "HH-TR-1006")
        String licensePlate,

        @Schema(description = "Type of the trailer", example = "BOX")
        TrailerType trailerType,

        @Schema(description = "Current status of the trailer", example = "MAINTENANCE")
        TrailerStatus status,

        @Schema(description = "Internal notes", example = "tires nearly depleted")
        String notes
) {
}
