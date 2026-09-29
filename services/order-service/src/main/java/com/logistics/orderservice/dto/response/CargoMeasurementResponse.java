package com.logistics.orderservice.dto.response;

import com.logistics.orderservice.domain.CargoItem;
import com.logistics.orderservice.domain.enums.CargoUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Request payload for creating a cargo measurement")
public record CargoMeasurementResponse(

        @Schema(
                description = "Generated technical identifier",
                example = "a275777a-59ac-40e1-a0ac-be0b9d027be1"
        )
        UUID measurementId,

        @Schema(
                description = "Measurement value",
                example = "12500"
        )
        BigDecimal value,

        @NotNull
        @Schema(
                description = "Measurement unit",
                example = "KILOGRAM"
        )
        CargoUnit unit
) {}
