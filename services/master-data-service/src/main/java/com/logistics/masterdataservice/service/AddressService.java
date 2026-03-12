package com.logistics.masterdataservice.service;

import com.logistics.masterdataservice.domain.Address;
import com.logistics.masterdataservice.dto.response.PagedResponse;
import com.logistics.masterdataservice.dto.request.AddressRequest;
import com.logistics.masterdataservice.dto.response.AddressResponse;
import com.logistics.masterdataservice.exception.ResourceNotFoundException;
import com.logistics.masterdataservice.mapper.AddressMapper;
import com.logistics.masterdataservice.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressResponse create(AddressRequest request) {
        Address address = AddressMapper.toEntity(request);
        Address saved = addressRepository.save(address);
        return AddressMapper.toResponse(saved);
    }

    public AddressResponse getById(UUID addressId) {
        Address address = loadAddress(addressId);
        return AddressMapper.toResponse(address);
    }

    public PagedResponse<AddressResponse> getAll(Pageable pageable) {
        Page<AddressResponse> page = addressRepository.findAll(pageable)
                .map(AddressMapper::toResponse);

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

    public AddressResponse delete(UUID addressId) {
        Address address = loadAddress(addressId);
        addressRepository.delete(address);
        return AddressMapper.toResponse(address);
    }

    public AddressResponse update(UUID addressId, AddressRequest request) {
        Address address = loadAddress(addressId);

        applyAddressUpdates(address, request);

        Address saved = addressRepository.save(address);
        return AddressMapper.toResponse(saved);
    }

    private Address loadAddress(UUID addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address",  addressId));
    }

    private void applyAddressUpdates(Address address, AddressRequest request) {
        address.setStreet(request.street());
        address.setHouseNumber(request.houseNumber());
        address.setPostalCode(request.postalCode());
        address.setCity(request.city());
        address.setCountry(request.country());
        address.setAdditionalLine(request.additionalLine());
    }
}