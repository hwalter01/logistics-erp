package com.logistics.masterdataservice.dto.request.searchrequest;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Search filters for customers")
public record CustomerSearchRequest(

        @Schema(description = "Exact customer number")
        String customerNumber,

        @Schema(description = "Exact VAT number")
        String vatNumber,

        @Schema(description = "Partial customer name")
        String name,

        @Schema(description = "Filter by POD requirement")
        Boolean podRequired
) {}
