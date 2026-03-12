package com.logistics.masterdataservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Request payload for creating or updating a customer")
public record CustomerRequest(

        @Schema(description = "Unique customer business number", example = "CUST-1001")
        @NotBlank
        String customerNumber,

        @Schema(description = "Customer display name", example = "NordSteel GmbH")
        @NotBlank
        String name,

        @Schema(description = "VAT number", example = "DE123456789")
        String vatNumber,

        @Schema(description = "Contact email", example = "logistics@nordsteel.de")
        String contactEmail,

        @Schema(description = "Contact phone", example = "+49 40 1234567")
        String contactPhone,

        @Schema(description = "Internal notes", example = "POD required for all deliveries")
        String notes,

        @Schema(description = "Whether proof of delivery is required", example = "true")
        @NotNull
        Boolean podRequired,

        @Schema(description = "Referenced address ID", example = "f0616a1a-95eb-4b6b-9151-92427595a4c6")
        @NotNull
        UUID addressId
) {
}