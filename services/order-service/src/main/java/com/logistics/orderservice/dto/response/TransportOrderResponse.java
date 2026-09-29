package com.logistics.orderservice.dto.response;

import com.logistics.orderservice.domain.enums.OrderStatus;
import com.logistics.orderservice.dto.request.create.CargoItemCreateRequest;
import com.logistics.orderservice.dto.request.create.OrderStopCreateRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(description = "Request payload for creating a transport order")
public record TransportOrderResponse(

        @Schema(
                description = "Generated technical identifier",
                example = "a275777a-59ac-40e1-a0ac-be0b9d027be1"
        )
        UUID orderId,

        @Schema(
                description = "Generated, globally unique, immutable business identifier",
                example = "ORD-NS-2026-0001"
        )
        String orderNumber,

        @Schema(
                description = "External reference to Master Data Customer",
                example = "a275777a-59ac-40e1-a0ac-be0b9d027be1"
        )
        UUID customerId,

        @Schema(
                description = "Customer-provided reference; unique per customer",
                example = "PO-4711"
        )
        String customerReference,

        @Schema(
                description = "Handling/equipment requirements",
                example = "Flammable goods"
        )
        String specialRequirements,

        @Schema(
                description = "Lifecycle state; created as `DRAFT` by application logic",
                example = "DRAFT"
        )
        OrderStatus status,

        @Schema(
                description = "Ordered list of stops for the transport order. At least one PICKUP and one DELIVERY are required.",
                example = """
                [
                  {
                    "sequenceNumber": 1,
                    "type": "PICKUP",
                    "locationId": "a275777a-59ac-40e1-a0ac-be0b9d027be1",
                    "timeWindowStart": "2026-09-15T08:00:00Z",
                    "timeWindowEnd": "2026-09-15T10:00:00Z",
                    "instructions": "Loading at gate 3",
                    "reference": "PICKUP-4711"
                  },
                  {
                    "sequenceNumber": 2,
                    "type": "DELIVERY",
                    "locationId": "4d932b58-13b0-4c14-961b-b334ebca98c2",
                    "timeWindowStart": "2026-09-15T14:00:00Z",
                    "timeWindowEnd": "2026-09-15T16:00:00Z",
                    "instructions": "Report to warehouse office",
                    "reference": "DELIVERY-4711"
                  }
                ]
                """
        )
        List<OrderStopResponse> orderStops,

        @Schema(
                description = "Cargo items belonging to the order"
        )
        List<CargoItemResponse> cargoItems,

        @Schema(
                description = "Created At"
        )
        Instant createdAt,

        @Schema(
                description = "Updated At"
        )
        Instant updatedAt,

        @Schema(
                description = "Row Version"
        )
        long rowVersion
) {
}
