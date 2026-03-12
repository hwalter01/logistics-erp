package com.logistics.masterdataservice.controller;

import com.logistics.masterdataservice.domain.enums.DriverStatus;
import com.logistics.masterdataservice.domain.enums.EmploymentType;
import com.logistics.masterdataservice.dto.response.PagedResponse;
import com.logistics.masterdataservice.dto.request.DriverRequest;
import com.logistics.masterdataservice.dto.request.searchrequest.DriverSearchRequest;
import com.logistics.masterdataservice.dto.response.DriverResponse;
import com.logistics.masterdataservice.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Validation error"),
        @ApiResponse(responseCode = "404", description = "Resource not found"),
        @ApiResponse(responseCode = "409", description = "Duplicate resource"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
})
@Tag(name = "Drivers", description = "Operations for managing drivers")
public class DriverController {

    private final DriverService driverService;

    @PostMapping
    @Operation(summary = "Create driver")
    @ApiResponse(responseCode = "200", description = "Driver created successfully")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "409", description = "Duplicate driver number")
    public DriverResponse create(@Valid @RequestBody DriverRequest request) {
        return driverService.create(request);
    }

    @GetMapping("/{driverId}")
    @Operation(summary = "Get driver by ID")
    @ApiResponse(responseCode = "200", description = "Driver found")
    @ApiResponse(responseCode = "404", description = "Driver not found")
    public DriverResponse getById(
            @Parameter(description = "Driver ID", required = true)
            @PathVariable UUID driverId
    ) {
        return driverService.getById(driverId);
    }

    @GetMapping
    @Operation(
            summary = "Search drivers",
            description = "Returns a paginated list of drivers. All provided filters are combined using logical AND."
    )
    @ApiResponse(responseCode = "200", description = "Drivers retrieved successfully")
    public PagedResponse<DriverResponse> search(
            @Parameter(description = "Filter by exact driver number")
            @RequestParam(required = false) String driverNumber,

            @Parameter(description = "Filter by partial first name, case-insensitive")
            @RequestParam(required = false) String firstName,

            @Parameter(description = "Filter by partial last name, case-insensitive")
            @RequestParam(required = false) String lastName,

            @Parameter(description = "Filter by employment type")
            @RequestParam(required = false) EmploymentType employmentType,

            @Parameter(description = "Filter by driver status")
            @RequestParam(required = false) DriverStatus status,

            @PageableDefault(size = 20, sort = "driverNumber") Pageable pageable
    ) {
        DriverSearchRequest request = new DriverSearchRequest(driverNumber, firstName, lastName, employmentType, status);
        return driverService.search(request, pageable);
    }

    @PutMapping("/{driverId}")
    @Operation(summary = "Update driver")
    @ApiResponse(responseCode = "200", description = "Driver updated successfully")
    @ApiResponse(responseCode = "404", description = "Driver or address not found")
    @ApiResponse(responseCode = "409", description = "Duplicate driver number")
    public DriverResponse update(
            @PathVariable UUID driverId,
            @Valid @RequestBody DriverRequest request
    ) {
        return driverService.update(driverId, request);
    }

    @DeleteMapping("/{driverId}")
    @Operation(summary = "Delete driver")
    @ApiResponse(responseCode = "200", description = "Driver deleted successfully")
    @ApiResponse(responseCode = "404", description = "Driver not found")
    public DriverResponse delete(@PathVariable UUID driverId) {
        return driverService.delete(driverId);
    }
}
