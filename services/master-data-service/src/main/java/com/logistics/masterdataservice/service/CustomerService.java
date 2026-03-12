package com.logistics.masterdataservice.service;

import com.logistics.masterdataservice.domain.Address;
import com.logistics.masterdataservice.domain.Customer;
import com.logistics.masterdataservice.dto.response.PagedResponse;
import com.logistics.masterdataservice.dto.request.CustomerRequest;
import com.logistics.masterdataservice.dto.request.searchrequest.CustomerSearchRequest;
import com.logistics.masterdataservice.dto.response.CustomerResponse;
import com.logistics.masterdataservice.exception.DuplicateResourceException;
import com.logistics.masterdataservice.exception.ResourceNotFoundException;
import com.logistics.masterdataservice.mapper.CustomerMapper;
import com.logistics.masterdataservice.repository.AddressRepository;
import com.logistics.masterdataservice.repository.CustomerRepository;
import com.logistics.masterdataservice.specification.CustomerSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    public CustomerResponse create(CustomerRequest request){
        validateCustomerNumber(request.customerNumber());

        Address address = loadAddress(request.addressId());

        Customer customer = CustomerMapper.toEntity(request, address);

        Customer saved =  customerRepository.save(customer);

        return CustomerMapper.toResponse(saved);
    }

    public CustomerResponse getById(UUID customerId){
        Customer customer = loadCustomer(customerId);

        return CustomerMapper.toResponse(customer);
    }

    public PagedResponse<CustomerResponse> getAll(Pageable pageable) {
        Page<CustomerResponse> page = customerRepository.findAll(pageable)
                .map(CustomerMapper::toResponse);

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

    public PagedResponse<CustomerResponse> search(CustomerSearchRequest request, Pageable pageable) {
        Page<CustomerResponse> page = customerRepository
                .findAll(CustomerSpecification.withFilters(request), pageable)
                .map(CustomerMapper::toResponse);

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

    public CustomerResponse delete(UUID customerId) {
        Customer customer = loadCustomer(customerId);

        customerRepository.delete(customer);

        return CustomerMapper.toResponse(customer);
    }

    public CustomerResponse update(UUID customerId, CustomerRequest request) {
        Customer customer = loadCustomer(customerId);
        Address address = loadAddress(request.addressId());

        //check if the new customer number is already used, if it is different from the previous
        if (!request.customerNumber().equals(customer.getCustomerNumber())) {
            validateCustomerNumber(customer.getCustomerNumber());
        }
        applyCustomerUpdates(customer, request, address);
        Customer saved =  customerRepository.save(customer);

        return CustomerMapper.toResponse(saved);
    }

    private Customer loadCustomer(UUID customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer",  customerId));
    }

    private Address loadAddress(UUID addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address",  addressId));
    }

    private void validateCustomerNumber(String customerNumber) {
        if (customerRepository.existsByCustomerNumber(customerNumber)) {
            throw new DuplicateResourceException(
                    "Customer number already exists: " + customerNumber
            );
        }
    }

    public void applyCustomerUpdates(Customer customer, CustomerRequest request, Address address){
        customer.setCustomerNumber(request.customerNumber());
        customer.setName(request.name());
        customer.setVatNumber(request.vatNumber());
        customer.setContactEmail(request.contactEmail());
        customer.setContactPhone(request.contactPhone());
        customer.setPodRequired(request.podRequired());
        customer.setAddress(address);
    }
}
