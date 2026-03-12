package com.logistics.masterdataservice.controller;

import com.logistics.masterdataservice.dto.response.PagedResponse;
import com.logistics.masterdataservice.dto.request.AddressRequest;
import com.logistics.masterdataservice.dto.response.AddressResponse;
import com.logistics.masterdataservice.service.AddressService;
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
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor

@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Validation error"),
        @ApiResponse(responseCode = "404", description = "Resource not found"),
        @ApiResponse(responseCode = "409", description = "Duplicate resource"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
})
@Tag(name = "Addresses", description = "Operations for managing addresses")
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    @Operation(
            summary = "Create address",
            description = "Creates a new address that can be referenced by customers, drivers, or locations."
    )
    @ApiResponse(responseCode = "200", description = "Address created successfully")
    @ApiResponse(responseCode = "400", description = "Validation error")
    public AddressResponse create(
            @Valid @RequestBody AddressRequest request
    ) {
        return addressService.create(request);
    }

    @GetMapping("/{addressId}")
    @Operation(
            summary = "Get address by ID",
            description = "Returns the address with the specified identifier."
    )
    @ApiResponse(responseCode = "200", description = "Address found")
    @ApiResponse(responseCode = "404", description = "Address not found")
    public AddressResponse getById(

            @Parameter(
                    description = "Unique identifier of the address",
                    required = true,
                    example = "f0616a1a-95eb-4b6b-9151-92427595a4c6"
            )
            @PathVariable UUID addressId
    ) {
        return addressService.getById(addressId);
    }

    @GetMapping
    @Operation(
            summary = "Get all addresses",
            description = "Returns a paginated list of all addresses."
    )
    @ApiResponse(responseCode = "200", description = "Addresses retrieved successfully")
    public PagedResponse<AddressResponse> getAll(

            @Parameter(
                    description = "Pagination and sorting configuration"
            )
            @PageableDefault(size = 20, sort = "postalCode")
            Pageable pageable
    ) {
        return addressService.getAll(pageable);
    }

    @PutMapping("/{addressId}")
    @Operation(
            summary = "Update address",
            description = "Updates the address with the specified identifier."
    )
    @ApiResponse(responseCode = "200", description = "Address updated successfully")
    @ApiResponse(responseCode = "404", description = "Address not found")
    @ApiResponse(responseCode = "400", description = "Validation error")
    public AddressResponse update(

            @Parameter(
                    description = "Unique identifier of the address",
                    required = true,
                    example = "f0616a1a-95eb-4b6b-9151-92427595a4c6"
            )
            @PathVariable UUID addressId,

            @Valid @RequestBody AddressRequest request
    ) {
        return addressService.update(addressId, request);
    }

    @DeleteMapping("/{addressId}")
    @Operation(
            summary = "Delete address",
            description = "Deletes the address with the specified identifier."
    )
    @ApiResponse(responseCode = "200", description = "Address deleted successfully")
    @ApiResponse(responseCode = "404", description = "Address not found")
    public AddressResponse delete(

            @Parameter(
                    description = "Unique identifier of the address",
                    required = true,
                    example = "f0616a1a-95eb-4b6b-9151-92427595a4c6"
            )
            @PathVariable UUID addressId
    ) {
        return addressService.delete(addressId);
    }
}