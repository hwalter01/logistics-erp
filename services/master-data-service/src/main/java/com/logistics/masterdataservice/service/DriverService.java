package com.logistics.masterdataservice.service;

import com.logistics.masterdataservice.domain.Address;
import com.logistics.masterdataservice.domain.Driver;
import com.logistics.masterdataservice.dto.request.createrequest.DriverCreateRequest;
import com.logistics.masterdataservice.dto.request.updaterequest.DriverUpdateRequest;
import com.logistics.masterdataservice.dto.response.PagedResponse;
import com.logistics.masterdataservice.dto.request.searchrequest.DriverSearchRequest;
import com.logistics.masterdataservice.dto.response.DriverResponse;
import com.logistics.masterdataservice.exception.DuplicateResourceException;
import com.logistics.masterdataservice.exception.ResourceNotFoundException;
import com.logistics.masterdataservice.mapper.DriverMapper;
import com.logistics.masterdataservice.repository.AddressRepository;
import com.logistics.masterdataservice.repository.DriverRepository;
import com.logistics.masterdataservice.specification.DriverSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DriverService {
    private final DriverRepository driverRepository;
    private final AddressRepository addressRepository;

    @Transactional
    public DriverResponse create(DriverCreateRequest request) {
        validateDriverNumber(request.driverNumber());

        Address address = loadAddress(request.addressId());
        Driver driver = DriverMapper.toEntity(request, address);

        Driver saved =  driverRepository.save(driver);
        return DriverMapper.toResponse(saved);
    }

    public DriverResponse getById(UUID driverId) {
        Driver driver = loadDriver(driverId);
        return DriverMapper.toResponse(driver);
    }

    public PagedResponse<DriverResponse> search(DriverSearchRequest request, Pageable pageable) {
        Page<DriverResponse> page = driverRepository
                .findAll(DriverSpecification.withFilters(request), pageable)
                .map(DriverMapper::toResponse);

        return new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }

    @Transactional
    public DriverResponse delete(UUID driverId) {
        Driver driver = loadDriver(driverId);
        driverRepository.delete(driver);
        return DriverMapper.toResponse(driver);
    }

    @Transactional
    public DriverResponse update(UUID driverId, @Valid DriverUpdateRequest request) {
        Driver driver = loadDriver(driverId);
        Address address = loadAddress(request.addressId());

        applyDriverUpdates(driver, request, address);

        Driver saved =  driverRepository.save(driver);
        return DriverMapper.toResponse(saved);
    }

    private Driver loadDriver(UUID driverId) {
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver",  driverId));
    }

    private Address loadAddress(UUID addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address",  addressId));
    }

    private void validateDriverNumber(String driverNumber) {
        if (driverRepository.existsByDriverNumber(driverNumber)) {
            throw new DuplicateResourceException(
                    "Driver number already exists: " + driverNumber
            );
        }
    }

    private void applyDriverUpdates(Driver driver, DriverUpdateRequest request, Address address) {
        driver.setFirstName(request.firstName());
        driver.setLastName(request.lastName());
        driver.setEmail(request.email());
        driver.setPhone(request.phone());
        driver.setLicenseNumber(request.licenseNumber());
        driver.setEmploymentType(request.employmentType());
        driver.setStatus(request.status());
        driver.setAddress(address);
    }


}
