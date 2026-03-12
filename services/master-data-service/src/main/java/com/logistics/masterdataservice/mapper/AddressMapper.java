package com.logistics.masterdataservice.mapper;

import com.logistics.masterdataservice.domain.Address;
import com.logistics.masterdataservice.dto.request.AddressRequest;
import com.logistics.masterdataservice.dto.response.AddressResponse;

public final class AddressMapper {

    private AddressMapper() {
    }

    public static Address toEntity(AddressRequest request) {
        return Address.builder()
                .street(request.street())
                .houseNumber(request.houseNumber())
                .postalCode(request.postalCode())
                .city(request.city())
                .country(request.country())
                .additionalLine(request.additionalLine())
                .build();
    }

    public static AddressResponse toResponse(Address address) {
        return new AddressResponse(
                address.getAddressId(),
                address.getStreet(),
                address.getHouseNumber(),
                address.getPostalCode(),
                address.getCity(),
                address.getCountry(),
                address.getAdditionalLine()
        );
    }
}