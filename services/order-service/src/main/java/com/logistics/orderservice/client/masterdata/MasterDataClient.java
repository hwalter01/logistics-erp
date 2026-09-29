package com.logistics.orderservice.client.masterdata;

import com.logistics.orderservice.exceptions.MasterDataNotFoundException;
import com.logistics.orderservice.exceptions.MasterDataUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class MasterDataClient {

    private final RestClient restClient;

    public MasterDataClient(
            @Value("${master-data.base-url}") String baseUrl
    ) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public MasterDataCustomerResponse getCustomer(UUID customerId) {
        try {
            return restClient.get()
                    .uri("/api/v1/customers/{customerId}", customerId)
                    .retrieve()
                    .body(MasterDataCustomerResponse.class);

        } catch (HttpClientErrorException.NotFound ex) {
            throw new MasterDataNotFoundException("Customer", customerId);

        } catch (ResourceAccessException ex) {
            throw new MasterDataUnavailableException(ex);
        }
    }

    public MasterDataLocationResponse getLocation(UUID locationId) {
        try {
            return restClient.get()
                    .uri("/api/v1/customers/{locationId}", locationId)
                    .retrieve()
                    .body(MasterDataLocationResponse.class);

        } catch (HttpClientErrorException.NotFound ex) {
            throw new MasterDataNotFoundException("Location", locationId);

        } catch (ResourceAccessException ex) {
            throw new MasterDataUnavailableException(ex);
        }
    }
}