package com.logistics.masterdataservice.integration;

import com.logistics.masterdataservice.dto.request.AddressRequest;
import com.logistics.masterdataservice.dto.request.LocationRequest;
import com.logistics.masterdataservice.dto.request.createrequest.CustomerCreateRequest;
import com.logistics.masterdataservice.dto.response.AddressResponse;
import com.logistics.masterdataservice.dto.response.CustomerResponse;
import com.logistics.masterdataservice.dto.response.LocationResponse;
import com.logistics.masterdataservice.dto.response.PagedResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LocationControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private AddressResponse createAddress(String street, String city) throws Exception {
        AddressRequest request = new AddressRequest(
                street,
                "10",
                "99701",
                city,
                "Germany",
                "Location integration test"
        );

        String response = mockMvc.perform(post("/api/v1/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(response, AddressResponse.class);
    }

    private CustomerResponse createCustomer(UUID addressId) throws Exception {
        String suffix = UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();

        CustomerCreateRequest request = new CustomerCreateRequest(
                "CUST-LOC-" + suffix,
                "Location Test Customer " + suffix,
                "DELOC" + suffix,
                "location." + suffix.toLowerCase() + "@example.com",
                "+49 123 999999",
                "Location integration test customer",
                true,
                addressId
        );

        String response = mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(response, CustomerResponse.class);
    }

    private LocationResponse createLocation(
            UUID customerId,
            UUID addressId,
            String name
    ) throws Exception {
        LocationRequest request = new LocationRequest(
                customerId,
                addressId,
                name,
                "Max Mustermann",
                "+49 40 123456",
                "location@example.com",
                "Report to gate A"
        );

        String response = mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(response, LocationResponse.class);
    }

    @Test
    void shouldCreateLocation() throws Exception {
        AddressResponse address =
                createAddress("Location Create Street", "Location Create City");
        CustomerResponse customer =
                createCustomer(address.addressId());

        LocationRequest request = new LocationRequest(
                customer.customerId(),
                address.addressId(),
                "Integration Warehouse",
                "Jane Doe",
                "+49 40 111111",
                "warehouse@example.com",
                "Use loading ramp 3"
        );

        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locationId").exists())
                .andExpect(jsonPath("$.customerId").value(customer.customerId().toString()))
                .andExpect(jsonPath("$.addressId").value(address.addressId().toString()))
                .andExpect(jsonPath("$.name").value("Integration Warehouse"))
                .andExpect(jsonPath("$.contactPerson").value("Jane Doe"))
                .andExpect(jsonPath("$.contactPhone").value("+49 40 111111"))
                .andExpect(jsonPath("$.contactEmail").value("warehouse@example.com"))
                .andExpect(jsonPath("$.siteInstructions").value("Use loading ramp 3"));
    }

    @Test
    void shouldGetLocationById() throws Exception {
        AddressResponse address =
                createAddress("Location Lookup Street", "Location Lookup City");
        CustomerResponse customer =
                createCustomer(address.addressId());

        LocationResponse created = createLocation(
                customer.customerId(),
                address.addressId(),
                "Lookup Warehouse"
        );

        mockMvc.perform(get("/api/v1/locations/{locationId}", created.locationId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locationId").value(created.locationId().toString()))
                .andExpect(jsonPath("$.name").value("Lookup Warehouse"));
    }

    @Test
    void shouldUpdateLocation() throws Exception {
        AddressResponse firstAddress =
                createAddress("First Location Street", "First Location City");
        CustomerResponse firstCustomer =
                createCustomer(firstAddress.addressId());

        LocationResponse created = createLocation(
                firstCustomer.customerId(),
                firstAddress.addressId(),
                "Original Location"
        );

        AddressResponse secondAddress =
                createAddress("Updated Location Street", "Updated Location City");
        CustomerResponse secondCustomer =
                createCustomer(secondAddress.addressId());

        LocationRequest updateRequest = new LocationRequest(
                secondCustomer.customerId(),
                secondAddress.addressId(),
                "Updated Location",
                "Updated Contact",
                "+49 40 222222",
                "updated.location@example.com",
                "Use updated gate"
        );

        mockMvc.perform(put("/api/v1/locations/{locationId}", created.locationId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locationId").value(created.locationId().toString()))
                .andExpect(jsonPath("$.customerId").value(secondCustomer.customerId().toString()))
                .andExpect(jsonPath("$.addressId").value(secondAddress.addressId().toString()))
                .andExpect(jsonPath("$.name").value("Updated Location"))
                .andExpect(jsonPath("$.contactPerson").value("Updated Contact"))
                .andExpect(jsonPath("$.contactPhone").value("+49 40 222222"))
                .andExpect(jsonPath("$.contactEmail").value("updated.location@example.com"))
                .andExpect(jsonPath("$.siteInstructions").value("Use updated gate"));
    }

    @Test
    void shouldSearchLocationByName() throws Exception {
        AddressResponse address =
                createAddress("Search Location Street", "Search Location City");
        CustomerResponse customer =
                createCustomer(address.addressId());

        LocationResponse expected = createLocation(
                customer.customerId(),
                address.addressId(),
                "Unique Integration Warehouse"
        );

        String response = mockMvc.perform(get("/api/v1/locations")
                        .param("name", "Unique Integration Warehouse")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "name"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PagedResponse<LocationResponse> page = objectMapper.readValue(
                response,
                new TypeReference<PagedResponse<LocationResponse>>() {}
        );

        assertThat(page.content().stream()
                .map(LocationResponse::locationId))
                .contains(expected.locationId());
    }

    @Test
    void shouldReturn404WhenCustomerDoesNotExist() throws Exception {
        AddressResponse address =
                createAddress("Missing Customer Street", "Missing Customer City");

        LocationRequest request = new LocationRequest(
                UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff"),
                address.addressId(),
                "Invalid Customer Location",
                null,
                null,
                null,
                null
        );

        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenAddressDoesNotExist() throws Exception {
        AddressResponse customerAddress =
                createAddress("Customer Base Street", "Customer Base City");
        CustomerResponse customer =
                createCustomer(customerAddress.addressId());

        LocationRequest request = new LocationRequest(
                customer.customerId(),
                UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff"),
                "Invalid Address Location",
                null,
                null,
                null,
                null
        );

        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteLocation() throws Exception {
        AddressResponse address =
                createAddress("Delete Location Street", "Delete Location City");
        CustomerResponse customer =
                createCustomer(address.addressId());

        LocationResponse created = createLocation(
                customer.customerId(),
                address.addressId(),
                "Delete Integration Location"
        );

        mockMvc.perform(delete("/api/v1/locations/{locationId}", created.locationId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locationId").value(created.locationId().toString()));

        mockMvc.perform(get("/api/v1/locations/{locationId}", created.locationId()))
                .andExpect(status().isNotFound());
    }

/*
    ----------------------------------------
    -----------ADDITIONAL FILTERS-----------
    ----------------------------------------
     */

    @Test
    void shouldSearchLocationByCustomerId() throws Exception {
        AddressResponse firstAddress =
                createAddress("Customer Filter Street 1", "Customer Filter City 1");
        CustomerResponse expectedCustomer =
                createCustomer(firstAddress.addressId());

        AddressResponse secondAddress =
                createAddress("Customer Filter Street 2", "Customer Filter City 2");
        CustomerResponse otherCustomer =
                createCustomer(secondAddress.addressId());

        LocationResponse expectedLocation = createLocation(
                expectedCustomer.customerId(),
                firstAddress.addressId(),
                "Customer Filter Expected Location"
        );

        LocationResponse otherLocation = createLocation(
                otherCustomer.customerId(),
                secondAddress.addressId(),
                "Customer Filter Other Location"
        );

        String response = mockMvc.perform(get("/api/v1/locations")
                        .param("customerId", expectedCustomer.customerId().toString())
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "name"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PagedResponse<LocationResponse> page = objectMapper.readValue(
                response,
                new TypeReference<PagedResponse<LocationResponse>>() {}
        );

        assertThat(page.content().stream()
                .map(LocationResponse::locationId))
                .contains(expectedLocation.locationId())
                .doesNotContain(otherLocation.locationId());
    }

    @Test
    void shouldSearchLocationByCity() throws Exception {
        AddressResponse expectedAddress =
                createAddress("City Filter Street 1", "Unique Filter City 9981");
        CustomerResponse expectedCustomer =
                createCustomer(expectedAddress.addressId());

        AddressResponse otherAddress =
                createAddress("City Filter Street 2", "Different Filter City 9982");
        CustomerResponse otherCustomer =
                createCustomer(otherAddress.addressId());

        LocationResponse expectedLocation = createLocation(
                expectedCustomer.customerId(),
                expectedAddress.addressId(),
                "City Filter Expected Location"
        );

        LocationResponse otherLocation = createLocation(
                otherCustomer.customerId(),
                otherAddress.addressId(),
                "City Filter Other Location"
        );

        String response = mockMvc.perform(get("/api/v1/locations")
                        .param("city", "Unique Filter City 9981")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "name"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PagedResponse<LocationResponse> page = objectMapper.readValue(
                response,
                new TypeReference<PagedResponse<LocationResponse>>() {}
        );

        assertThat(page.content().stream()
                .map(LocationResponse::locationId))
                .contains(expectedLocation.locationId())
                .doesNotContain(otherLocation.locationId());
    }

    @Test
    void shouldCombineLocationFilters() throws Exception {
        AddressResponse matchingAddress =
                createAddress("Combined Filter Street 1", "Combined City 9983");
        CustomerResponse matchingCustomer =
                createCustomer(matchingAddress.addressId());

        AddressResponse wrongCityAddress =
                createAddress("Combined Filter Street 2", "Wrong Combined City 9984");

        LocationResponse expectedLocation = createLocation(
                matchingCustomer.customerId(),
                matchingAddress.addressId(),
                "Combined Unique Warehouse 9983"
        );

        LocationResponse wrongCityLocation = createLocation(
                matchingCustomer.customerId(),
                wrongCityAddress.addressId(),
                "Combined Unique Warehouse 9983"
        );

        LocationResponse wrongNameLocation = createLocation(
                matchingCustomer.customerId(),
                matchingAddress.addressId(),
                "Different Warehouse 9983"
        );

        String response = mockMvc.perform(get("/api/v1/locations")
                        .param("customerId", matchingCustomer.customerId().toString())
                        .param("name", "Combined Unique Warehouse 9983")
                        .param("city", "Combined City 9983")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "name"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PagedResponse<LocationResponse> page = objectMapper.readValue(
                response,
                new TypeReference<PagedResponse<LocationResponse>>() {}
        );

        assertThat(page.totalElements()).isEqualTo(1);

        assertThat(page.content().stream()
                .map(LocationResponse::locationId))
                .containsExactly(expectedLocation.locationId())
                .doesNotContain(
                        wrongCityLocation.locationId(),
                        wrongNameLocation.locationId()
                );
    }
}
