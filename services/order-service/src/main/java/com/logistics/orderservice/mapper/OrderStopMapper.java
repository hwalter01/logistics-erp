package com.logistics.orderservice.mapper;

import com.logistics.orderservice.domain.OrderStop;
import com.logistics.orderservice.dto.request.create.OrderStopCreateRequest;
import com.logistics.orderservice.dto.request.update.OrderStopUpdateRequest;
import com.logistics.orderservice.dto.response.OrderStopResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderStopMapper {

    public OrderStop toEntity(OrderStopCreateRequest request) {
        return OrderStop.builder()
                .sequenceNumber(request.sequenceNumber())
                .type(request.type())
                .locationId(request.locationId())
                .timeWindowStart(request.timeWindowStart())
                .timeWindowEnd(request.timeWindowEnd())
                .instructions(request.instructions())
                .reference(request.reference())
                .build();
    }

    public void updateEntity(
            OrderStop stop,
            OrderStopUpdateRequest request
    ) {
        stop.setSequenceNumber(request.sequenceNumber());
        stop.setType(request.type());
        stop.setLocationId(request.locationId());
        stop.setTimeWindowStart(request.timeWindowStart());
        stop.setTimeWindowEnd(request.timeWindowEnd());
        stop.setInstructions(request.instructions());
        stop.setReference(request.reference());
    }

    public static OrderStopResponse toResponse(OrderStop orderStop) {
        return new OrderStopResponse(
                orderStop.getStopId(),
                orderStop.getSequenceNumber(),
                orderStop.getType(),
                orderStop.getLocationId(),
                orderStop.getTimeWindowStart(),
                orderStop.getTimeWindowEnd(),
                orderStop.getInstructions(),
                orderStop.getReference()
        );
    }
}

