package com.logistics.masterdataservice.specification;

import com.logistics.masterdataservice.domain.Customer;
import com.logistics.masterdataservice.domain.Driver;
import com.logistics.masterdataservice.dto.request.searchrequest.CustomerSearchRequest;
import com.logistics.masterdataservice.dto.request.searchrequest.DriverSearchRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class DriverSpecification {

    public  DriverSpecification() {
    }

    public static Specification<Driver> withFilters(DriverSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.driverNumber() != null && !request.driverNumber().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("driverNumber"), request.driverNumber()));
            }

            if (request.firstName() != null && !request.firstName().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("firstName")),
                                "%" + request.firstName().toLowerCase() + "%"
                        )
                );
            }

            if (request.lastName() != null && !request.lastName().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("lastName")),
                                "%" + request.lastName().toLowerCase() + "%"
                        )
                );
            }

            if (request.status() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.status()));
            }

            if (request.employmentType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("employmentType"), request.employmentType()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }


}
