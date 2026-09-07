package com.logistics.masterdataservice.dto.request.updaterequest;

import com.logistics.masterdataservice.domain.enums.VehicleStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request payload for creating or updating a vehicle")
public record VehicleUpdateRequest(

        @Schema(description = "Vehicle license plate number", example = "HH-NC-1001")
        @NotBlank String licensePlate,

        @Schema(description = "International vehicle identification number", example = "WDB9340321L123456")
        String vin,

        @Schema(description = "Brand of the manufacturer", example = "Mercedes-Benz")
        String brand,

        @Schema(description = "Model the vehicle", example = "Actros")
        String model,

        @Schema(description = "Current status of the vehicle", example = "ACTIVE")
        @NotNull VehicleStatus status,

        @Schema(description = "Internal notes", example = "Long Haul truck")
        String notes
) {
}
