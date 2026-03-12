package com.logistics.masterdataservice.dto.response;

import com.logistics.masterdataservice.domain.enums.VehicleStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Request payload for creating or updating a vehicle")
public record VehicleResponse (

        @Schema(description = "Unique identifier of the vehicle", example = "b13f272b-307d-44dc-891b-33c7147a0aba")
        UUID vehicleId,



        @Schema(description = "Unique vehicle business number", example = "VEH-0001")
        String vehicleNumber,

        @Schema(description = "Vehicle license plate number", example = "HH-NC-1001")
        String licensePlate,

        @Schema(description = "International vehicle identification number", example = "WDB9340321L123456")
        String vin,

        @Schema(description = "Brand of the manufacturer", example = "Mercedes-Benz")
        String brand,

        @Schema(description = "Model the vehicle", example = "Actros")
        String model,

        @Schema(description = "Current status of the vehicle", example = "ACTIVE")
        VehicleStatus status,

        @Schema(description = "Internal notes", example = "Long Haul truck")
        String notes
) {
}
