package com.logistics.orderservice.mapper;

import com.logistics.orderservice.domain.TransportOrder;
import com.logistics.orderservice.dto.request.create.TransportOrderCreateRequest;
import com.logistics.orderservice.dto.request.update.TransportOrderUpdateRequest;
import com.logistics.orderservice.dto.response.TransportOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransportOrderMapper {
    private final CargoItemMapper cargoItemMapper;
    private final OrderStopMapper orderStopMapper;

    public TransportOrder toEntity(TransportOrderCreateRequest request) {

        TransportOrder transportOrder = TransportOrder.builder()
                .customerId(request.customerId())
                .customerReference(request.customerReference())
                .specialRequirements(request.specialRequirements())
                .build();

        request.cargoItems().stream()
                .map(cargoItemMapper::toEntity)
                .forEach(transportOrder::addCargoItem);

        request.orderStops().stream()
                .map(orderStopMapper::toEntity)
                .forEach(transportOrder::addStop);

        return transportOrder;
    }

    public void updateEntity(
            TransportOrder order,
            TransportOrderUpdateRequest request
    ) {
        order.setCustomerId(request.customerId());
        order.setCustomerReference(request.customerReference());
        order.setSpecialRequirements(request.specialRequirements());
    }

    public TransportOrderResponse toResponse(TransportOrder transportOrder) {
        return new  TransportOrderResponse(
                transportOrder.getOrderId(),
                transportOrder.getOrderNumber(),
                transportOrder.getCustomerId(),
                transportOrder.getCustomerReference(),
                transportOrder.getSpecialRequirements(),
                transportOrder.getStatus(),
                transportOrder.getStops().stream()
                        .map(OrderStopMapper::toResponse)
                        .toList(),
                transportOrder.getCargoItems().stream()
                        .map(cargoItemMapper::toResponse)
                        .toList(),
                transportOrder.getCreatedAt(),
                transportOrder.getUpdatedAt(),
                transportOrder.getRowVersion()
        );
    }
}
