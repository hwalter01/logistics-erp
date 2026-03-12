package com.logistics.masterdataservice.specification;

import com.logistics.masterdataservice.domain.Customer;
import com.logistics.masterdataservice.dto.request.searchrequest.CustomerSearchRequest;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public final class CustomerSpecification {

    private CustomerSpecification() {
    }

    public static Specification<Customer> withFilters(CustomerSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.customerNumber() != null && !request.customerNumber().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("customerNumber"), request.customerNumber()));
            }

            if (request.vatNumber() != null && !request.vatNumber().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("vatNumber"), request.vatNumber()));
            }

            if (request.name() != null && !request.name().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                "%" + request.name().toLowerCase() + "%"
                        )
                );
            }

            if (request.podRequired() != null) {
                predicates.add(criteriaBuilder.equal(root.get("podRequired"), request.podRequired()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
