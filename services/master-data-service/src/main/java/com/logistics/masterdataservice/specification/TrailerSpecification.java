package com.logistics.masterdataservice.specification;

import com.logistics.masterdataservice.domain.Trailer;
import com.logistics.masterdataservice.dto.request.searchrequest.TrailerSearchRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TrailerSpecification {

    private TrailerSpecification(){}

    public static Specification<Trailer> withFilters(TrailerSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.trailerNumber() != null && !request.trailerNumber().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("trailerNumber"), request.trailerNumber()));
            }

            if (request.licensePlate() != null && !request.licensePlate().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("licensePlate")),
                                "%" + request.licensePlate().toLowerCase() + "%"
                        )
                );
            }

            if (request.trailerType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("trailerType"), request.trailerType()));
            }

            if (request.status() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.status()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
