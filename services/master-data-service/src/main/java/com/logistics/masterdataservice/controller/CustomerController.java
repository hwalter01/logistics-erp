package com.logistics.masterdataservice.controller;

import com.logistics.masterdataservice.dto.response.PagedResponse;
import com.logistics.masterdataservice.dto.request.CustomerRequest;
import com.logistics.masterdataservice.dto.request.searchrequest.CustomerSearchRequest;
import com.logistics.masterdataservice.dto.response.CustomerResponse;
import com.logistics.masterdataservice.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Validation error"),
        @ApiResponse(responseCode = "404", description = "Resource not found"),
        @ApiResponse(responseCode = "409", description = "Duplicate resource"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
})
@Tag(name = "Customers", description = "Operations for managing customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @Operation(
            summary = "Create customer",
            description = "Creates a new customer in the master data service"
    )
    @ApiResponse(responseCode = "200", description = "Customer created successfully")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "409", description = "Duplicate customer number")
    public CustomerResponse create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Customer creation payload",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CustomerRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "customerNumber": "CUST-1001",
                                              "name": "NordSteel GmbH",
                                              "vatNumber": "DE123456789",
                                              "contactEmail": "logistics@nordsteel.de",
                                              "contactPhone": "+49 40 1234567",
                                              "notes": "POD required for all deliveries",
                                              "podRequired": true,
                                              "addressId": "f0616a1a-95eb-4b6b-9151-92427595a4c6"
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody CustomerRequest request
    ) {
        return customerService.create(request);
    }

    @GetMapping("/{customerId}")
    @Operation(summary = "Get customer by ID")
    @ApiResponse(responseCode = "200", description = "Customer found")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public CustomerResponse getById(
            @Parameter(description = "Customer ID", required = true)
            @PathVariable UUID customerId
    ) {
        return customerService.getById(customerId);
    }

    @GetMapping
    @Operation(
            summary = "Search customers",
            description = "Returns paginated customers filtered by the provided query parameters. All filters are combined with logical AND."
    )
    @ApiResponse(responseCode = "200", description = "Search executed successfully")
    public PagedResponse<CustomerResponse> search(
            @Parameter(description = "Filter by exact customer number")
            @RequestParam(required = false) String customerNumber,

            @Parameter(description = "Filter by exact VAT number")
            @RequestParam(required = false) String vatNumber,

            @Parameter(description = "Filter by partial customer name, case-insensitive")
            @RequestParam(required = false) String name,

            @Parameter(description = "Filter by POD requirement")
            @RequestParam(required = false) Boolean podRequired,

            @PageableDefault(size = 20, sort = "customerNumber")
            Pageable pageable
    ) {
        CustomerSearchRequest request = new CustomerSearchRequest(
                customerNumber,
                vatNumber,
                name,
                podRequired
        );
        return customerService.search(request, pageable);
    }

    @PutMapping("/{customerId}")
    @Operation(summary = "Update customer")
    @ApiResponse(responseCode = "200", description = "Customer updated successfully")
    @ApiResponse(responseCode = "404", description = "Customer or address not found")
    @ApiResponse(responseCode = "409", description = "Duplicate customer number")
    public CustomerResponse update(
            @PathVariable UUID customerId,
            @Valid @RequestBody CustomerRequest request
    ) {
        return customerService.update(customerId, request);
    }

    @DeleteMapping("/{customerId}")
    @Operation(summary = "Delete customer")
    @ApiResponse(responseCode = "200", description = "Customer deleted successfully")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public CustomerResponse delete(@PathVariable UUID customerId) {
        return customerService.delete(customerId);
    }
}