package com.logistics.masterdataservice.service;

import com.logistics.masterdataservice.domain.Vehicle;
import com.logistics.masterdataservice.dto.response.PagedResponse;
import com.logistics.masterdataservice.dto.request.VehicleRequest;
import com.logistics.masterdataservice.dto.request.searchrequest.VehicleSearchRequest;
import com.logistics.masterdataservice.dto.response.VehicleResponse;
import com.logistics.masterdataservice.exception.DuplicateResourceException;
import com.logistics.masterdataservice.exception.ResourceNotFoundException;
import com.logistics.masterdataservice.mapper.VehicleMapper;
import com.logistics.masterdataservice.repository.VehicleRepository;
import com.logistics.masterdataservice.specification.VehicleSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleResponse create(VehicleRequest request){
        validateVehicleNumber(request.vehicleNumber());
        Vehicle vehicle = VehicleMapper.toEntity(request);
        Vehicle saved  = vehicleRepository.save(vehicle);
        return VehicleMapper.toResponse(saved);
    }



    public VehicleResponse getById(UUID vehicleId){
        Vehicle vehicle = loadVehicle(vehicleId);
        return VehicleMapper.toResponse(vehicle);
    }

    public PagedResponse<VehicleResponse> search(VehicleSearchRequest request, Pageable pageable) {
        Page<VehicleResponse> page = vehicleRepository
                .findAll(VehicleSpecification.withFilters(request), pageable)
                .map(VehicleMapper::toResponse);

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

    public VehicleResponse delete(UUID vehicleId) {
        Vehicle vehicle = loadVehicle(vehicleId);
        vehicleRepository.delete(vehicle);
        return VehicleMapper.toResponse(vehicle);
    }

    public VehicleResponse update(UUID vehicleId, VehicleRequest request){
        Vehicle vehicle = loadVehicle(vehicleId);
        if (!request.vehicleNumber().equals(vehicle.getVehicleNumber())) {
            validateVehicleNumber(request.vehicleNumber());
        }
        applyVehicleUpdates(vehicle, request);
        Vehicle saved  = vehicleRepository.save(vehicle);
        return VehicleMapper.toResponse(saved);
    }

    private void validateVehicleNumber(String vehicleNumber) {
        if (vehicleRepository.existsByVehicleNumber(vehicleNumber)) {
            throw new DuplicateResourceException(
                    "Vehicle number already exists: " + vehicleNumber
            );
        }
    }

    private Vehicle loadVehicle(UUID vehicleId){
        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle",  vehicleId));
    }

    private void applyVehicleUpdates(Vehicle vehicle, VehicleRequest request){
        vehicle.setVehicleNumber(request.vehicleNumber());
        vehicle.setLicensePlate(request.licensePlate());
        vehicle.setVin(request.vin());
        vehicle.setBrand(request.brand());
        vehicle.setModel(request.model());
        vehicle.setStatus(request.status());
        vehicle.setNotes(request.notes());
    }
}
