package com.logistics.masterdataservice.mapper;

import com.logistics.masterdataservice.domain.Address;
import com.logistics.masterdataservice.domain.Customer;
import com.logistics.masterdataservice.dto.request.createrequest.CustomerCreateRequest;
import com.logistics.masterdataservice.dto.response.CustomerResponse;

public final class CustomerMapper {

    private CustomerMapper() {
    }

     public static Customer toEntity(CustomerCreateRequest request, Address address) {
        return Customer.builder()
                .customerNumber(request.customerNumber())
                .name(request.name())
                .vatNumber(request.vatNumber())
                .contactEmail(request.contactEmail())
                .contactPhone(request.contactPhone())
                .notes(request.notes())
                .podRequired(request.podRequired())
                .address(address)
                .build();
    }


    public static CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getCustomerId(),
                customer.getCustomerNumber(),
                customer.getName(),
                customer.getVatNumber(),
                customer.getContactEmail(),
                customer.getContactPhone(),
                customer.getNotes(),
                customer.isPodRequired(),
                customer.getAddress().getAddressId()
        );
    }
}
