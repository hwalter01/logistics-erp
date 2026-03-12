package com.logistics.masterdataservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Standard error response returned by the API")
public record ErrorResponse(

        @Schema(description = "Timestamp when the error occurred",
                example = "2026-03-09T10:15:30Z")
        Instant timestamp,

        @Schema(description = "HTTP status code",
                example = "404")
        int status,

        @Schema(description = "Error type",
                example = "Not Found")
        String error,

        @Schema(description = "Detailed error message",
                example = "Customer not found: 11111111-1111-1111-1111-111111111111")
        String message,

        @Schema(description = "Request path",
                example = "/api/v1/customers/11111111-1111-1111-1111-111111111111")
        String path
) {
}
