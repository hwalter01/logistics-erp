package com.logistics.orderservice.service;

import com.logistics.orderservice.repository.OrderNumberSequenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderNumberGenerator {

    private final OrderNumberSequenceRepository orderNumberSequenceRepository;

    public String generate(UUID customerId, String customerShortCode) {
        int year = Year.now().getValue();

        int nextNumber = orderNumberSequenceRepository.nextNumber(customerId, year);

        return "ORD-"
                + customerShortCode
                + "-"
                + year
                + "-"
                + String.format("%04d", nextNumber);
    }
}
