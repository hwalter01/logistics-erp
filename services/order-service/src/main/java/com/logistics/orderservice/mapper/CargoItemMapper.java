package com.logistics.orderservice.mapper;

import com.logistics.orderservice.domain.CargoItem;
import com.logistics.orderservice.dto.request.create.CargoItemCreateRequest;
import com.logistics.orderservice.dto.request.update.CargoItemUpdateRequest;
import com.logistics.orderservice.dto.response.CargoItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CargoItemMapper {

    private final CargoMeasurementMapper cargoMeasurementMapper;

    public CargoItem toEntity(CargoItemCreateRequest request) {

        CargoItem cargoItem = CargoItem.builder()
                .description(request.description())
                .notes(request.notes())
                .build();

        request.measurements().stream()
                .map(cargoMeasurementMapper::toEntity)
                .forEach(cargoItem::addMeasurement);

        return cargoItem;
    }

    public void updateEntity(
            CargoItem cargoItem,
            CargoItemUpdateRequest request
    ) {
        cargoItem.setDescription(request.description());
        cargoItem.setNotes(request.notes());

        cargoItem.getCargoMeasurements().clear();

        request.measurements().stream()
                .map(cargoMeasurementMapper::toEntity)
                .forEach(cargoItem::addMeasurement);
    }

    public CargoItemResponse toResponse(CargoItem cargoItem) {
        return new CargoItemResponse(
                cargoItem.getCargoItemId(),
                cargoItem.getDescription(),
                cargoItem.getCargoMeasurements().stream()
                        .map(cargoMeasurementMapper::toResponse)
                        .toList(),
                cargoItem.getNotes()
        );
    }
}

