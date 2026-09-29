package com.logistics.orderservice.dto.request.update;

import com.logistics.orderservice.dto.request.create.CargoMeasurementCreateRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(description = "Request payload for updating a cargo item")
public record CargoItemUpdateRequest(

        @NotBlank
        @Schema(
                description = "Description of the transported goods",
                example = "Steel coils"
        )
        String description,

        @NotEmpty
        @Valid
        @Schema(
                description = "Measurements describing quantity, weight, volume or loading dimensions"
        )
        List<CargoMeasurementCreateRequest> measurements,

        @Schema(
                description = "Optional additional information about the cargo",
                example = "Keep dry during transport"
        )
        String notes
) {}
