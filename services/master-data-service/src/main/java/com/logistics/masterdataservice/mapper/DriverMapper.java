package com.logistics.masterdataservice.mapper;

import com.logistics.masterdataservice.domain.Address;
import com.logistics.masterdataservice.domain.Driver;
import com.logistics.masterdataservice.domain.enums.DriverStatus;
import com.logistics.masterdataservice.dto.request.DriverRequest;
import com.logistics.masterdataservice.dto.response.DriverResponse;

public final class DriverMapper {

    private DriverMapper() {
    }

    public static Driver toEntity(DriverRequest request, Address address) {
        return Driver.builder()
                .driverNumber(request.driverNumber())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phone(request.phone())
                .licenseNumber(request.licenseNumber())
                .employmentType(request.employmentType())
                .status(request.status())
                .address(address)
                .build();
    }

    public static DriverResponse toResponse(Driver driver) {
        return new DriverResponse(
                driver.getDriverId(),
                driver.getDriverNumber(),
                driver.getFirstName(),
                driver.getLastName(),
                driver.getPhone(),
                driver.getEmail(),
                driver.getLicenseNumber(),
                driver.getEmploymentType(),
                driver.getStatus(),
                driver.getAddress().getAddressId()
        );
    }
}
