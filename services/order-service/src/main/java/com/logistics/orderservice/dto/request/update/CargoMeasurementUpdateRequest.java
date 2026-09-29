package com.logistics.orderservice.dto.request.update;

import com.logistics.orderservice.domain.enums.CargoUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Request payload for updating a cargo measurement")
public record CargoMeasurementUpdateRequest(

        @NotNull
        @DecimalMin(value = "0.001")
        @Digits(integer = 12, fraction = 3)
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
