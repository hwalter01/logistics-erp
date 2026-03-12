package com.logistics.masterdataservice.dto.request.searchrequest;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Search filters for locations")
public record LocationSearchRequest(

        @Schema(description = "Exact customer ID", example = "11111111-1111-1111-1111-111111111111")
        UUID customerId,

        @Schema(description = "Partial location name match, case-insensitive", example = "Warehouse")
        String name,

        @Schema(description = "Partial city match, case-insensitive", example = "Hamburg")
        String city
) {
}
