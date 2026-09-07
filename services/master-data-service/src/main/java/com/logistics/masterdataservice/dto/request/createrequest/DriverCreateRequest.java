package com.logistics.masterdataservice.dto.request.createrequest;

import com.logistics.masterdataservice.domain.enums.DriverStatus;
import com.logistics.masterdataservice.domain.enums.EmploymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Request payload for creating or updating a driver")
public record DriverCreateRequest(

        @Schema(description = "Unique driver business number", example = "DRV-0001")
        @NotBlank String driverNumber,

        @Schema(description = "Drivers first legal name", example = "Max")
        @NotBlank String firstName,

        @Schema(description = "Drivers last name", example = "Mueller")
        @NotBlank String lastName,

        @Schema(description = "Drivers contact phone number", example = "+49 171 1234567")
        String phone,

        @Schema(description = "Drivers contact email address", example = "max.mueller@nordcargo.de")
        String email,

        @Schema(description = "Number of the drivers driving license", example = "B12345678")
        @NotBlank String licenseNumber,

        @Schema(description = "Type of employment", example = "EMPLOYEE")
        @NotNull EmploymentType employmentType,

        @Schema(description = "Current status of the driver", example = "ACTIVE")
        @NotNull DriverStatus status,

        @Schema(description = "Referenced address ID", example = "f0616a1a-95eb-4b6b-9151-92427595a4c6")
        UUID addressId
) {
}
