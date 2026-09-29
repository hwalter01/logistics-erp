package com.logistics.orderservice.mapper;

import com.logistics.orderservice.domain.CargoMeasurement;
import com.logistics.orderservice.dto.request.create.CargoMeasurementCreateRequest;
import com.logistics.orderservice.dto.request.update.CargoMeasurementUpdateRequest;
import com.logistics.orderservice.dto.response.CargoMeasurementResponse;
import org.springframework.stereotype.Component;

@Component
public class CargoMeasurementMapper {

    public CargoMeasurement toEntity(CargoMeasurementCreateRequest request) {
        return CargoMeasurement.builder()
                .value(request.value())
                .unit(request.unit())
                .build();
    }

    public void updateEntity(
            CargoMeasurement measurement,
            CargoMeasurementUpdateRequest request
    ) {
        measurement.setValue(request.value());
        measurement.setUnit(request.unit());
    }

    public CargoMeasurementResponse toResponse(CargoMeasurement measurement) {
        return new CargoMeasurementResponse(
                measurement.getMeasurementId(),
                measurement.getValue(),
                measurement.getUnit()
        );
    }
}
