package com.logistics.orderservice.dto.response;

import com.logistics.orderservice.domain.TransportOrder;
import com.logistics.orderservice.dto.request.create.CargoMeasurementCreateRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

@Schema(description = "Request payload for creating a cargo item")
public record CargoItemResponse(

        @Schema(
                description = "Generated technical identifier",
                example = "a275777a-59ac-40e1-a0ac-be0b9d027be1"
        )
        UUID cargoItemId,

        @Schema(
                description = "Description of the transported goods",
                example = "Steel coils"
        )
        String description,

        @Schema(
                description = "Measurements describing quantity, weight, volume or loading dimensions"
        )
        List<CargoMeasurementResponse> cargoMeasurements,

        @Schema(
                description = "Optional additional information about the cargo",
                example = "Keep dry during transport"
        )
        String notes
) {}
