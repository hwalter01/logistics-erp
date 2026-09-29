package com.logistics.orderservice.dto.request.update;

import com.logistics.orderservice.domain.enums.StopType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Request payload for updating a transport stop")
public record OrderStopUpdateRequest(

        @NotNull
        @Positive
        @Schema(
                description = "Position of the stop within the order",
                example = "1"
        )
        Integer sequenceNumber,

        @NotNull
        @Schema(
                description = "Type of stop",
                example = "PICKUP"
        )
        StopType type,

        @NotNull
        @Schema(
                description = "Reference to an existing location in the Master Data Service",
                example = "a275777a-59ac-40e1-a0ac-be0b9d027be1"
        )
        UUID locationId,

        @Schema(
                description = "Beginning of the planned service time window",
                example = "2026-09-15T08:00:00Z"
        )
        Instant timeWindowStart,

        @Schema(
                description = "End of the planned service time window",
                example = "2026-09-15T10:00:00Z"
        )
        Instant timeWindowEnd,

        @Schema(
                description = "Instructions for the driver at this stop",
                example = "Report to gate 3 and ask for warehouse staff"
        )
        String instructions,

        @Schema(
                description = "Optional reference specific to this stop",
                example = "PICKUP-4711"
        )
        String reference
) {
}
