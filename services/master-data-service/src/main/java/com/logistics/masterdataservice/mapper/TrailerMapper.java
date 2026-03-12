package com.logistics.masterdataservice.mapper;

import com.logistics.masterdataservice.domain.Trailer;
import com.logistics.masterdataservice.dto.request.TrailerRequest;
import com.logistics.masterdataservice.dto.response.TrailerResponse;

public final class TrailerMapper {

    private TrailerMapper() {}

    public static Trailer toEntity(TrailerRequest request) {
        return Trailer.builder()
                .trailerNumber(request.trailerNumber())
                .licensePlate(request.licensePlate())
                .trailerType(request.trailerType())
                .status(request.status())
                .notes(request.notes())
                .build();
    }

    public static TrailerResponse toResponse(Trailer trailer) {
        return new TrailerResponse(
                trailer.getTrailerId(),
                trailer.getTrailerNumber(),
                trailer.getLicensePlate(),
                trailer.getTrailerType(),
                trailer.getStatus(),
                trailer.getNotes()
        );
    }
}
