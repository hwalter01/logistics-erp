package com.logistics.masterdataservice.mapper;

import com.logistics.masterdataservice.domain.Vehicle;
import com.logistics.masterdataservice.dto.request.VehicleRequest;
import com.logistics.masterdataservice.dto.response.VehicleResponse;

public final class VehicleMapper {

    private VehicleMapper() {}

    public static Vehicle toEntity(VehicleRequest request) {
        return Vehicle.builder()
                .vehicleNumber(request.vehicleNumber())
                .licensePlate(request.licensePlate())
                .vin(request.vin())
                .brand(request.brand())
                .model(request.model())
                .status(request.status())
                .notes(request.notes())
                .build();
    }

    public static VehicleResponse toResponse(Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getVehicleId(),
                vehicle.getVehicleNumber(),
                vehicle.getLicensePlate(),
                vehicle.getVin(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getStatus(),
                vehicle.getNotes()
        );
    }
}
