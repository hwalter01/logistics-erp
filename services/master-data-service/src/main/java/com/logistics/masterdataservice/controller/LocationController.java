package com.logistics.masterdataservice.controller;

import com.logistics.masterdataservice.dto.response.PagedResponse;
import com.logistics.masterdataservice.dto.request.LocationRequest;
import com.logistics.masterdataservice.dto.request.searchrequest.LocationSearchRequest;
import com.logistics.masterdataservice.dto.response.LocationResponse;
import com.logistics.masterdataservice.service.LocationService;
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
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Validation error"),
        @ApiResponse(responseCode = "404", description = "Resource not found"),
        @ApiResponse(responseCode = "409", description = "Duplicate resource"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
})
@Tag(name = "Locations", description = "Operations for managing locations")
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    @Operation(summary = "Create location")
    @ApiResponse(responseCode = "200", description = "Location created successfully")
    @ApiResponse(responseCode = "400", description = "Validation error")
    public LocationResponse create(@Valid @RequestBody LocationRequest request) {
        return locationService.create(request);
    }

    @GetMapping("/{locationId}")
    @Operation(summary = "Get location by ID")
    @ApiResponse(responseCode = "200", description = "Location found")
    @ApiResponse(responseCode = "404", description = "Location not found")
    public LocationResponse getById(
            @Parameter(description = "Location ID", required = true)
            @PathVariable UUID locationId
    ) {
        return locationService.getById(locationId);
    }

    @GetMapping
    @Operation(
            summary = "Search locations",
            description = "Returns a paginated list of locations. All provided filters are combined using logical AND."
    )
    @ApiResponse(responseCode = "200", description = "Locations retrieved successfully")
    public PagedResponse<LocationResponse> search(
            @Parameter(description = "Filter by exact customer ID")
            @RequestParam(required = false) UUID customerId,

            @Parameter(description = "Filter by partial location name, case-insensitive")
            @RequestParam(required = false) String name,

            @Parameter(description = "Filter by partial city, case-insensitive")
            @RequestParam(required = false) String city,

            @PageableDefault(size = 20, sort = "name") Pageable pageable
    ) {
        LocationSearchRequest request = new LocationSearchRequest(customerId, name, city);
        return locationService.search(request, pageable);
    }

    @PutMapping("/{locationId}")
    @Operation(summary = "Update location")
    @ApiResponse(responseCode = "200", description = "Location updated successfully")
    @ApiResponse(responseCode = "404", description = "Location, customer, or address not found")
    public LocationResponse update(
            @PathVariable UUID locationId,
            @Valid @RequestBody LocationRequest request
    ) {
        return locationService.update(locationId, request);
    }

    @DeleteMapping("/{locationId}")
    @Operation(summary = "Delete location")
    @ApiResponse(responseCode = "200", description = "Location deleted successfully")
    @ApiResponse(responseCode = "404", description = "Location not found")
    public LocationResponse delete(@PathVariable UUID locationId) {
        return locationService.delete(locationId);
    }
}
