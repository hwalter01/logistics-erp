package com.logistics.masterdataservice.dto.request.searchrequest;

import com.logistics.masterdataservice.domain.enums.DriverStatus;
import com.logistics.masterdataservice.domain.enums.EmploymentType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Search filters for drivers")
public record DriverSearchRequest(

        @Schema(description = "Exact driver number", example = "DRV-1001")
        String driverNumber,

        @Schema(description = "Partial first name match, case-insensitive", example = "Max")
        String firstName,

        @Schema(description = "Partial last name match, case-insensitive", example = "Müller")
        String lastName,

        @Schema(description = "Filter by employment type")
        EmploymentType employmentType,

        @Schema(description = "Filter by driver status")
        DriverStatus status
) {
}
