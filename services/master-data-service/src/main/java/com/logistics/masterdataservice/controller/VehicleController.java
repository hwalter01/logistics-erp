package com.logistics.masterdataservice.controller;

import com.logistics.masterdataservice.domain.enums.VehicleStatus;
import com.logistics.masterdataservice.dto.request.createrequest.VehicleCreateRequest;
import com.logistics.masterdataservice.dto.request.updaterequest.VehicleUpdateRequest;
import com.logistics.masterdataservice.dto.response.PagedResponse;
import com.logistics.masterdataservice.dto.request.searchrequest.VehicleSearchRequest;
import com.logistics.masterdataservice.dto.response.VehicleResponse;
import com.logistics.masterdataservice.service.VehicleService;
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
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Validation error"),
        @ApiResponse(responseCode = "404", description = "Resource not found"),
        @ApiResponse(responseCode = "409", description = "Duplicate resource"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
})
@Tag(name = "Vehicles", description = "Operations for managing vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    @Operation(summary = "Create vehicle")
    @ApiResponse(responseCode = "200", description = "Vehicle created successfully")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "409", description = "Duplicate vehicle number or license plate")
    public VehicleResponse create(@Valid @RequestBody VehicleCreateRequest request) {
        return vehicleService.create(request);
    }

    @GetMapping("/{vehicleId}")
    @Operation(summary = "Get vehicle by ID")
    @ApiResponse(responseCode = "200", description = "Vehicle found")
    @ApiResponse(responseCode = "404", description = "Vehicle not found")
    public VehicleResponse getById(
            @Parameter(description = "Vehicle ID", required = true)
            @PathVariable UUID vehicleId
    ) {
        return vehicleService.getById(vehicleId);
    }

    @GetMapping
    @Operation(
            summary = "Search vehicles",
            description = "Returns a paginated list of vehicles. All provided filters are combined using logical AND."
    )
    @ApiResponse(responseCode = "200", description = "Vehicles retrieved successfully")
    public PagedResponse<VehicleResponse> search(
            @Parameter(description = "Filter by exact vehicle number")
            @RequestParam(required = false) String vehicleNumber,

            @Parameter(description = "Filter by exact license plate")
            @RequestParam(required = false) String licensePlate,

            @Parameter(description = "Filter by partial brand, case-insensitive")
            @RequestParam(required = false) String brand,

            @Parameter(description = "Filter by partial model, case-insensitive")
            @RequestParam(required = false) String model,

            @Parameter(description = "Filter by vehicle status")
            @RequestParam(required = false) VehicleStatus status,

            @PageableDefault(size = 20, sort = "vehicleNumber") Pageable pageable
    ) {
        VehicleSearchRequest request = new VehicleSearchRequest(vehicleNumber, licensePlate, brand, model, status);
        return vehicleService.search(request, pageable);
    }

    @PutMapping("/{vehicleId}")
    @Operation(summary = "Update vehicle")
    @ApiResponse(responseCode = "200", description = "Vehicle updated successfully")
    @ApiResponse(responseCode = "404", description = "Vehicle not found")
    @ApiResponse(responseCode = "409", description = "Duplicate vehicle number or license plate")
    public VehicleResponse update(
            @PathVariable UUID vehicleId,
            @Valid @RequestBody VehicleUpdateRequest request
    ) {
        return vehicleService.update(vehicleId, request);
    }

    @DeleteMapping("/{vehicleId}")
    @Operation(summary = "Delete vehicle")
    @ApiResponse(responseCode = "200", description = "Vehicle deleted successfully")
    @ApiResponse(responseCode = "404", description = "Vehicle not found")
    public VehicleResponse delete(@PathVariable UUID vehicleId) {
        return vehicleService.delete(vehicleId);
    }
}
