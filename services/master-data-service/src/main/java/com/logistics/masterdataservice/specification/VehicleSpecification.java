package com.logistics.masterdataservice.specification;

import com.logistics.masterdataservice.domain.Vehicle;
import com.logistics.masterdataservice.dto.request.searchrequest.VehicleSearchRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class VehicleSpecification {

    private VehicleSpecification() {}

    public static Specification<Vehicle> withFilters(VehicleSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.vehicleNumber() != null && !request.vehicleNumber().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("vehicleNumber"), request.vehicleNumber()));
            }

            if (request.licensePlate() != null && !request.licensePlate().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("licensePlate")),
                                "%" + request.licensePlate().toLowerCase() + "%"
                        )
                );
            }

            if (request.brand() != null && !request.brand().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("brand")),
                                "%" + request.brand().toLowerCase() + "%"
                        )
                );
            }

            if (request.model() != null && !request.model().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("model")),
                                "%" + request.model().toLowerCase() + "%"
                        )
                );
            }


            if (request.status() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.status()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
