package com.logistics.masterdataservice.dto.request.searchrequest;

import com.logistics.masterdataservice.domain.enums.VehicleStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Search filters for vehicles")
public record VehicleSearchRequest(

        @Schema(description = "Exact vehicle number", example = "TRUCK-1001")
        String vehicleNumber,

        @Schema(description = "Exact license plate", example = "HH-LG-1001")
        String licensePlate,

        @Schema(description = "Partial brand match, case-insensitive", example = "Mercedes")
        String brand,

        @Schema(description = "Partial model match, case-insensitive", example = "Actros")
        String model,

        @Schema(description = "Filter by vehicle status")
        VehicleStatus status
) {
}
