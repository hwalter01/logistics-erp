package com.logistics.masterdataservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Response payload for loading a customer")
public record CustomerResponse(

    UUID customerId,

    @Schema(description = "Unique customer business number", example = "CUST-1001")
    String customerNumber,

    @Schema(description = "Customer display name", example = "NordSteel GmbH")
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
    boolean podRequired,

    @Schema(description = "Referenced address ID", example = "f0616a1a-95eb-4b6b-9151-92427595a4c6")
    UUID addressId,

    @Schema(description = "Short identifier")
    String shortCode
){

}
