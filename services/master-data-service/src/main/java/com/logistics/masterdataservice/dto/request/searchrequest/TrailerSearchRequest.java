package com.logistics.masterdataservice.dto.request.searchrequest;

import com.logistics.masterdataservice.domain.enums.TrailerStatus;
import com.logistics.masterdataservice.domain.enums.TrailerType;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Search filters for trailers")
public record TrailerSearchRequest(

        @Schema(description = "Exact trailer number", example = "TRL-1001")
        String trailerNumber,

        @Schema(description = "Exact trailer license plate", example = "HH-TR-1001")
        String licensePlate,

        @Schema(description = "Filter by trailer type")
        TrailerType trailerType,

        @Schema(description = "Filter by trailer status")
        TrailerStatus status
) {
}
