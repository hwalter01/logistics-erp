package com.logistics.masterdataservice.dto.response;

import com.logistics.masterdataservice.domain.enums.DriverStatus;
import com.logistics.masterdataservice.domain.enums.EmploymentType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Response payload for loading a driver")
public record DriverResponse(

        @Schema(description = "Unique identifier of the driver", example = "7e3f0d3d-2e5f-4e1b-bc36-9b3b5d5e9c12")
        UUID driverId,



        @Schema(description = "Unique driver business number", example = "DRV-0001")
        String driverNumber,

        @Schema(description = "Drivers first legal name", example = "Max")
        String firstName,

        @Schema(description = "Drivers last name", example = "Mueller")
        String lastName,

        @Schema(description = "Drivers contact phone number", example = "+49 171 1234567")
        String phone,

        @Schema(description = "Drivers contact email address", example = "max.mueller@nordcargo.de")
        String email,

        @Schema(description = "Number of the drivers driving license", example = "B12345678")
        String licenseNumber,

        @Schema(description = "Type of employment", example = "EMPLOYEE")
        EmploymentType employmentType,

        @Schema(description = "Current status of the driver", example = "ACTIVE")
        DriverStatus status,

        @Schema(description = "Referenced address ID", example = "f0616a1a-95eb-4b6b-9151-92427595a4c6")
        UUID addressId
) {
}
