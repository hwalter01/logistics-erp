package com.logistics.masterdataservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request Payload for creating or updating an address")
public record AddressRequest(

        @Schema(description = "Street", example = "Hafenstraße")
        @NotBlank String street,

        @Schema(description = "House number", example = "10")
        @NotBlank String houseNumber,

        @Schema(description = "Postal code", example = "20095")
        @NotBlank String postalCode,

        @Schema(description = "City", example = "Hamburg")
        @NotBlank String city,

        @Schema(description = "Country", example = "Germany")
        @NotBlank String country,

        @Schema(description = "Additional line for extra information", example = "Warehouse B")
        String additionalLine
) {
}