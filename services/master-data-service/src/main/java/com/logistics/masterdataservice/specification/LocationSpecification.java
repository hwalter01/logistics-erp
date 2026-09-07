package com.logistics.masterdataservice.specification;

import com.logistics.masterdataservice.domain.Driver;
import com.logistics.masterdataservice.domain.Location;
import com.logistics.masterdataservice.dto.request.searchrequest.DriverSearchRequest;
import com.logistics.masterdataservice.dto.request.searchrequest.LocationSearchRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class LocationSpecification {

    private LocationSpecification() {}

    public static Specification<Location> withFilters(LocationSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.customerId() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("customer").get("customerId"),
                                request.customerId()
                        )
                );
            }

            if (request.name() != null && !request.name().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                "%" + request.name().toLowerCase() + "%"
                        )
                );
            }

            if (request.city() != null && !request.city().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get("address").get("city")
                                ),
                                "%" + request.city().toLowerCase() + "%"
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}
