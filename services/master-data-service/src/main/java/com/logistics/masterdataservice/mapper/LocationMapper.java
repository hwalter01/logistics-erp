package com.logistics.masterdataservice.mapper;

import com.logistics.masterdataservice.domain.Address;
import com.logistics.masterdataservice.domain.Customer;
import com.logistics.masterdataservice.domain.Location;
import com.logistics.masterdataservice.dto.request.LocationRequest;
import com.logistics.masterdataservice.dto.response.LocationResponse;

public final class LocationMapper {

    private LocationMapper() {
    }

    public static Location toEntity(LocationRequest request, Customer customer, Address address) {
        return Location.builder()
                .customer(customer)
                .address(address)
                .name(request.name())
                .contactPerson(request.contactPerson())
                .contactPhone(request.contactPhone())
                .contactEmail(request.contactEmail())
                .siteInstructions(request.siteInstructions())
                .build();
    }

    public static LocationResponse toResponse(Location location) {
        return new LocationResponse(
                location.getLocationId(),
                location.getCustomer().getCustomerId(),
                location.getAddress().getAddressId(),
                location.getName(),
                location.getContactPerson(),
                location.getContactPhone(),
                location.getContactEmail(),
                location.getSiteInstructions()
        );
    }
}
