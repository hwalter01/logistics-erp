package com.logistics.masterdataservice.service;

import com.logistics.masterdataservice.domain.Trailer;
import com.logistics.masterdataservice.dto.response.PagedResponse;
import com.logistics.masterdataservice.dto.request.TrailerRequest;
import com.logistics.masterdataservice.dto.request.searchrequest.TrailerSearchRequest;
import com.logistics.masterdataservice.dto.response.TrailerResponse;
import com.logistics.masterdataservice.exception.DuplicateResourceException;
import com.logistics.masterdataservice.exception.ResourceNotFoundException;
import com.logistics.masterdataservice.mapper.TrailerMapper;
import com.logistics.masterdataservice.repository.TrailerRepository;
import com.logistics.masterdataservice.specification.TrailerSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrailerService {
    private final TrailerRepository trailerRepository;

    public TrailerResponse create(TrailerRequest request){
        validateTrailerNumber(request.trailerNumber());
        Trailer trailer = TrailerMapper.toEntity(request);
        Trailer saved  = trailerRepository.save(trailer);
        return TrailerMapper.toResponse(saved);
    }

    public TrailerResponse getById(UUID trailerId){
        Trailer trailer = loadTrailer(trailerId);

        return TrailerMapper.toResponse(trailer);
    }

    public PagedResponse<TrailerResponse> search(TrailerSearchRequest request, Pageable pageable) {
        Page<TrailerResponse> page = trailerRepository
                .findAll(TrailerSpecification.withFilters(request), pageable)
                .map(TrailerMapper::toResponse);

        return new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }

    public TrailerResponse delete(UUID trailerId) {
        Trailer trailer = loadTrailer(trailerId);
        trailerRepository.delete(trailer);
        return TrailerMapper.toResponse(trailer);
    }

    public TrailerResponse update(UUID trailerId, TrailerRequest request) {
        Trailer trailer = loadTrailer(trailerId);

        if (!request.trailerNumber().equals(trailer.getTrailerNumber())) {
            validateTrailerNumber(request.trailerNumber());
        }

        applyTrailerUpdates(trailer, request);
        Trailer saved  = trailerRepository.save(trailer);
        return TrailerMapper.toResponse(saved);
    }

    private void validateTrailerNumber(String trailerNumber) {
        if (trailerRepository.existsByTrailerNumber(trailerNumber)) {
            throw new DuplicateResourceException(
                    "Trailer number already exists: " + trailerNumber
            );
        }
    }

    private Trailer loadTrailer(UUID trailerId){
        return trailerRepository.findById(trailerId)
                .orElseThrow(() -> new ResourceNotFoundException("Trailer",  trailerId));
    }

    private void applyTrailerUpdates(Trailer trailer, TrailerRequest request){
        trailer.setTrailerNumber(request.trailerNumber());
        trailer.setLicensePlate(request.licensePlate());
        trailer.setTrailerType(request.trailerType());
        trailer.setStatus(request.status());
        trailer.setNotes(request.notes());
    }

}
