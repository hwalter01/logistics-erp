package com.logistics.masterdataservice.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Employment type of the driver")
public enum EmploymentType {
    @Schema(description = "Driver is a company employee")
    EMPLOYEE,

    @Schema(description = "Driver works as subcontractor")
    SUBCONTRACTOR,

    @Schema(description = "Driver works as contractor")
    CONTRACTOR
}
