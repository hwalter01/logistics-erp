package com.logistics.masterdataservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;
@Schema(description = "Request payload for creating or updating a location")
public record LocationResponse(

        @Schema(description = "Unique identifier of the location", example = "284d0554-2750-452d-80bb-68a7737babe4")
        UUID locationId,



        @Schema(description = "Referenced customer ID", example = "5193e524-688f-48eb-8c58-f6e030bfcad2")
        UUID customerId,

        @Schema(description = "Referenced address ID", example = "f0616a1a-95eb-4b6b-9151-92427595a4c6")
        UUID addressId,

        @Schema(description = "Location display name", example = "Bavaria Chemicals Warehouse")
        String name,

        @Schema(description = "Name of the contact person", example = "Max Mueller")
        String contactPerson,

        @Schema(description = "Contact phone", example = "+49 40 1234567")
        String contactPhone,

        @Schema(description = "Contact email", example = "logistics@nordsteel.de")
        String contactEmail,

        @Schema(description = "Special instructions for the location", example = "Report to security gate")
        String siteInstructions
) {
}
