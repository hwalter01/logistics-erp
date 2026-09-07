package com.logistics.masterdataservice.controller;

import com.logistics.masterdataservice.domain.enums.TrailerStatus;
import com.logistics.masterdataservice.domain.enums.TrailerType;
import com.logistics.masterdataservice.dto.request.createrequest.TrailerCreateRequest;
import com.logistics.masterdataservice.dto.request.updaterequest.TrailerUpdateRequest;
import com.logistics.masterdataservice.dto.response.PagedResponse;
import com.logistics.masterdataservice.dto.request.searchrequest.TrailerSearchRequest;
import com.logistics.masterdataservice.dto.response.TrailerResponse;
import com.logistics.masterdataservice.service.TrailerService;
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
@RequestMapping("/api/v1/trailers")
@RequiredArgsConstructor
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Validation error"),
        @ApiResponse(responseCode = "404", description = "Resource not found"),
        @ApiResponse(responseCode = "409", description = "Duplicate resource"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
})
@Tag(name = "Trailers", description = "Operations for managing trailers")
public class TrailerController {

    private final TrailerService trailerService;

    @PostMapping
    @Operation(summary = "Create trailer")
    @ApiResponse(responseCode = "200", description = "Trailer created successfully")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "409", description = "Duplicate trailer number")
    public TrailerResponse create(@Valid @RequestBody TrailerCreateRequest request) {
        return trailerService.create(request);
    }

    @GetMapping("/{trailerId}")
    @Operation(summary = "Get trailer by ID")
    @ApiResponse(responseCode = "200", description = "Trailer found")
    @ApiResponse(responseCode = "404", description = "Trailer not found")
    public TrailerResponse getById(
            @Parameter(description = "Trailer ID", required = true)
            @PathVariable UUID trailerId
    ) {
        return trailerService.getById(trailerId);
    }

    @GetMapping
    @Operation(
            summary = "Search trailers",
            description = "Returns a paginated list of trailers. All provided filters are combined using logical AND."
    )
    @ApiResponse(responseCode = "200", description = "Trailers retrieved successfully")
    public PagedResponse<TrailerResponse> search(
            @Parameter(description = "Filter by exact trailer number")
            @RequestParam(required = false) String trailerNumber,

            @Parameter(description = "Filter by exact trailer license plate")
            @RequestParam(required = false) String licensePlate,

            @Parameter(description = "Filter by trailer type")
            @RequestParam(required = false) TrailerType trailerType,

            @Parameter(description = "Filter by trailer status")
            @RequestParam(required = false) TrailerStatus status,

            @PageableDefault(size = 20, sort = "trailerNumber") Pageable pageable
    ) {
        TrailerSearchRequest request = new TrailerSearchRequest(trailerNumber, licensePlate, trailerType, status);
        return trailerService.search(request, pageable);
    }

    @PutMapping("/{trailerId}")
    @Operation(summary = "Update trailer")
    @ApiResponse(responseCode = "200", description = "Trailer updated successfully")
    @ApiResponse(responseCode = "404", description = "Trailer not found")
    @ApiResponse(responseCode = "409", description = "Duplicate trailer number")
    public TrailerResponse update(
            @PathVariable UUID trailerId,
            @Valid @RequestBody TrailerUpdateRequest request
    ) {
        return trailerService.update(trailerId, request);
    }

    @DeleteMapping("/{trailerId}")
    @Operation(summary = "Delete trailer")
    @ApiResponse(responseCode = "200", description = "Trailer deleted successfully")
    @ApiResponse(responseCode = "404", description = "Trailer not found")
    public TrailerResponse delete(@PathVariable UUID trailerId) {
        return trailerService.delete(trailerId);
    }
}
