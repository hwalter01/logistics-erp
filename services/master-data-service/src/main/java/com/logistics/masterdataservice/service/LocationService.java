package com.logistics.masterdataservice.service;

import com.logistics.masterdataservice.domain.Address;
import com.logistics.masterdataservice.domain.Customer;
import com.logistics.masterdataservice.domain.Location;
import com.logistics.masterdataservice.dto.response.PagedResponse;
import com.logistics.masterdataservice.dto.request.LocationRequest;
import com.logistics.masterdataservice.dto.request.searchrequest.LocationSearchRequest;
import com.logistics.masterdataservice.dto.response.LocationResponse;
import com.logistics.masterdataservice.exception.ResourceNotFoundException;
import com.logistics.masterdataservice.mapper.LocationMapper;
import com.logistics.masterdataservice.repository.AddressRepository;
import com.logistics.masterdataservice.repository.CustomerRepository;
import com.logistics.masterdataservice.repository.LocationRepository;
import com.logistics.masterdataservice.specification.LocationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    public LocationResponse create(LocationRequest request) {
        Customer customer = loadCustomer(request.customerId());
        Address address = loadAddress(request.addressId());

        Location location = LocationMapper.toEntity(request, customer, address);

        Location saved = locationRepository.save(location);
        return LocationMapper.toResponse(saved);
    }

    public LocationResponse getById(UUID locationId) {
        Location location = loadLocation(locationId);
        return LocationMapper.toResponse(location);
    }

    public List<LocationResponse> getAll() {
        return locationRepository.findAll()
                .stream()
                .map(LocationMapper::toResponse)
                .toList();
    }

    public PagedResponse<LocationResponse> search(LocationSearchRequest request, Pageable pageable) {
        Page<LocationResponse> page = locationRepository
                .findAll(LocationSpecification.withFilters(request), pageable)
                .map(LocationMapper::toResponse);

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

    public LocationResponse delete(UUID locationId) {
        Location location = loadLocation(locationId);
        locationRepository.delete(location);
        return LocationMapper.toResponse(location);
    }

    public LocationResponse update(UUID locationId, LocationRequest request) {
        Location location = loadLocation(locationId);
        Customer customer = loadCustomer(request.customerId());
        Address address = loadAddress(request.addressId());

        applyLocationUpdates(location, request, address, customer);

        Location saved = locationRepository.save(location);

        return LocationMapper.toResponse(saved);
    }

    private Location loadLocation(UUID locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location",  locationId));
    }

    private Customer loadCustomer(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer",  id));
    }

    private Address loadAddress(UUID addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address",  addressId));
    }

    private void applyLocationUpdates(Location location, LocationRequest request, Address address, Customer customer) {
        location.setAddress(address);
        location.setCustomer(customer);
        location.setName(request.name());
        location.setContactPerson(request.contactPerson());
        location.setContactPhone(request.contactPhone());
        location.setContactEmail(request.contactEmail());
        location.setSiteInstructions(request.siteInstructions());
    }


}
