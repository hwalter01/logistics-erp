package com.logistics.orderservice.service;

import com.logistics.orderservice.client.masterdata.MasterDataClient;
import com.logistics.orderservice.client.masterdata.MasterDataCustomerResponse;
import com.logistics.orderservice.domain.TransportOrder;
import com.logistics.orderservice.domain.enums.CargoUnit;
import com.logistics.orderservice.domain.enums.OrderStatus;
import com.logistics.orderservice.domain.enums.StopType;
import com.logistics.orderservice.dto.request.create.CargoItemCreateRequest;
import com.logistics.orderservice.dto.request.create.CargoMeasurementCreateRequest;
import com.logistics.orderservice.dto.request.create.OrderStopCreateRequest;
import com.logistics.orderservice.dto.request.create.TransportOrderCreateRequest;
import com.logistics.orderservice.dto.response.TransportOrderResponse;
import com.logistics.orderservice.exceptions.DuplicateCustomerReferenceException;
import com.logistics.orderservice.exceptions.OrderValidationException;
import com.logistics.orderservice.mapper.TransportOrderMapper;
import com.logistics.orderservice.repository.TransportOrderRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final TransportOrderRepository transportOrderRepository;
    private final TransportOrderMapper transportOrderMapper;
    private final MasterDataClient masterDataClient;
    private final OrderNumberGenerator orderNumberGenerator;


    @Transactional
    public TransportOrderResponse create(TransportOrderCreateRequest request) {

        // 1. Business-Regeln innerhalb des Orders prüfen
        validateCreateRequest(request);


        // 2. Customer im Master Data Service prüfen
        //    und den ShortCode für die Ordernummer laden
        MasterDataCustomerResponse customer =
                masterDataClient.getCustomer(request.customerId());


        // 3. Alle verwendeten Locations prüfen
        Set<UUID> locationIds = request.orderStops().stream()
                .map(OrderStopCreateRequest::locationId)
                .collect(Collectors.toSet());

        for (UUID locationId : locationIds) {
            masterDataClient.getLocation(locationId);
        }


        // 4. CustomerReference darf beim selben Customer
        //    nicht bereits existieren
        if (request.customerReference() != null
                && !request.customerReference().isBlank()
                && transportOrderRepository
                .existsByCustomerIdAndCustomerReference(
                        request.customerId(),
                        request.customerReference()
                )) {

            throw new DuplicateCustomerReferenceException(
                    request.customerId(),
                    request.customerReference()
            );
        }


        // 5. Interne Ordernummer erzeugen
        String orderNumber = orderNumberGenerator.generate(
                request.customerId(),
                customer.shortCode()
        );


        // 6. Request -> Entity
        TransportOrder order =
                transportOrderMapper.toEntity(request);


        // 7. Systemverwaltete Werte setzen
        order.setOrderNumber(orderNumber);
        order.setStatus(OrderStatus.DRAFT);


        // 8. Komplettes Aggregate speichern
        TransportOrder savedOrder =
                transportOrderRepository.save(order);


        // 9. Entity -> Response
        return transportOrderMapper.toResponse(savedOrder);
    }


    private void validateCreateRequest(
            TransportOrderCreateRequest request
    ) {

        validateRequiredStopTypes(request.orderStops());

        validateUniqueStopSequences(request.orderStops());

        validateTimeWindows(request.orderStops());

        validateUniqueMeasurementUnits(request.cargoItems());
    }


    private void validateRequiredStopTypes(
            List<OrderStopCreateRequest> stops
    ) {

        boolean hasPickup = stops.stream()
                .anyMatch(stop ->
                        stop.type() == StopType.PICKUP
                );

        boolean hasDelivery = stops.stream()
                .anyMatch(stop ->
                        stop.type() == StopType.DELIVERY
                );


        if (!hasPickup || !hasDelivery) {
            throw new OrderValidationException(
                    "Order must contain at least one PICKUP and one DELIVERY stop"
            );
        }
    }


    private void validateUniqueStopSequences(
            List<OrderStopCreateRequest> stops
    ) {

        Set<Integer> sequences = new HashSet<>();

        for (OrderStopCreateRequest stop : stops) {

            if (!sequences.add(stop.sequenceNumber())) {

                throw new OrderValidationException(
                        "Duplicate stop sequence number: "
                                + stop.sequenceNumber()
                );
            }
        }
    }


    private void validateTimeWindows(
            List<OrderStopCreateRequest> stops
    ) {

        for (OrderStopCreateRequest stop : stops) {

            if (stop.timeWindowStart() != null
                    && stop.timeWindowEnd() != null
                    && stop.timeWindowStart()
                    .isAfter(stop.timeWindowEnd())) {

                throw new OrderValidationException(
                        "Time window start must not be after "
                                + "time window end for stop sequence "
                                + stop.sequenceNumber()
                );
            }
        }
    }


    private void validateUniqueMeasurementUnits(
            List<CargoItemCreateRequest> cargoItems
    ) {

        for (CargoItemCreateRequest cargoItem : cargoItems) {

            Set<CargoUnit> units = new HashSet<>();

            for (CargoMeasurementCreateRequest measurement
                    : cargoItem.measurements()) {

                if (!units.add(measurement.unit())) {

                    throw new OrderValidationException(
                            "Duplicate cargo measurement unit: "
                                    + measurement.unit()
                    );
                }
            }
        }
    }
}