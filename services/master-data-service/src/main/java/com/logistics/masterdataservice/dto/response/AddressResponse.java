package com.logistics.masterdataservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Response payload for loading an address")
public record AddressResponse(

        @Schema(description = "Unique identifier of the address", example = "a1a1a1a1-0000-0000-0000-000000000007")
        UUID addressId,

        @Schema(description = "Street", example = "Hafenstraße")
        String street,

        @Schema(description = "House number", example = "10")
        String houseNumber,

        @Schema(description = "Postal code", example = "20095")
        String postalCode,

        @Schema(description = "City", example = "Hamburg")
        String city,

        @Schema(description = "Country", example = "Germany")
        String country,

        @Schema(description = "Additional line for extra information", example = "Warehouse B")
        String additionalLine
) {
}