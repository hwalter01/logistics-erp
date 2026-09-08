package com.logistics.masterdataservice.integration;

import com.logistics.masterdataservice.dto.request.AddressRequest;
import com.logistics.masterdataservice.dto.request.createrequest.CustomerCreateRequest;
import com.logistics.masterdataservice.dto.response.AddressResponse;
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
public class AddressControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private AddressRequest validAddressRequest(String street, String postalCode, String city) {
        return new AddressRequest(
                street,
                "42",
                postalCode,
                city,
                "Germany",
                "Integration test address"
        );
    }

    private AddressResponse createAddress(AddressRequest request) throws Exception {
        String response = mockMvc.perform(post("/api/v1/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(response, AddressResponse.class);
    }

    @Test
    void shouldCreateAddress() throws Exception {
        AddressRequest request =
                validAddressRequest("Integration Street", "99801", "Integration City");

        mockMvc.perform(post("/api/v1/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addressId").exists())
                .andExpect(jsonPath("$.street").value(request.street()))
                .andExpect(jsonPath("$.houseNumber").value(request.houseNumber()))
                .andExpect(jsonPath("$.postalCode").value(request.postalCode()))
                .andExpect(jsonPath("$.city").value(request.city()))
                .andExpect(jsonPath("$.country").value(request.country()))
                .andExpect(jsonPath("$.additionalLine").value(request.additionalLine()));
    }

    @Test
    void shouldGetAddressById() throws Exception {
        AddressRequest request =
                validAddressRequest("Lookup Street", "99802", "Lookup City");

        AddressResponse created = createAddress(request);

        mockMvc.perform(get("/api/v1/addresses/{addressId}", created.addressId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addressId").value(created.addressId().toString()))
                .andExpect(jsonPath("$.street").value(request.street()))
                .andExpect(jsonPath("$.city").value(request.city()));
    }

    @Test
    void shouldUpdateAddress() throws Exception {
        AddressResponse created = createAddress(
                validAddressRequest("Old Street", "99803", "Old City")
        );

        AddressRequest updateRequest = new AddressRequest(
                "Updated Street",
                "99A",
                "99804",
                "Updated City",
                "Germany",
                "Updated additional line"
        );

        mockMvc.perform(put("/api/v1/addresses/{addressId}", created.addressId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addressId").value(created.addressId().toString()))
                .andExpect(jsonPath("$.street").value("Updated Street"))
                .andExpect(jsonPath("$.houseNumber").value("99A"))
                .andExpect(jsonPath("$.postalCode").value("99804"))
                .andExpect(jsonPath("$.city").value("Updated City"))
                .andExpect(jsonPath("$.country").value("Germany"))
                .andExpect(jsonPath("$.additionalLine").value("Updated additional line"));
    }

    @Test
    void shouldReturn404WhenAddressDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/addresses/{addressId}",
                        "ffffffff-ffff-ffff-ffff-ffffffffffff"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400ForInvalidAddress() throws Exception {
        AddressRequest request = new AddressRequest(
                "",
                "",
                "",
                "",
                "",
                null
        );

        mockMvc.perform(post("/api/v1/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldListAddresses() throws Exception {
        AddressRequest request =
                validAddressRequest("List Test Street", "99805", "List Test City");

        AddressResponse created = createAddress(request);

        String response = mockMvc.perform(get("/api/v1/addresses")
                        .param("page", "0")
                        .param("size", "100")
                        .param("sort", "postalCode"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PagedResponse<AddressResponse> page = objectMapper.readValue(
                response,
                new TypeReference<PagedResponse<AddressResponse>>() {}
        );

        assertThat(page.content().stream()
                .map(AddressResponse::addressId))
                .contains(created.addressId());
    }

    @Test
    void shouldDeleteAddress() throws Exception {
        AddressResponse created = createAddress(
                validAddressRequest("Delete Street", "99806", "Delete City")
        );

        mockMvc.perform(delete("/api/v1/addresses/{addressId}", created.addressId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addressId").value(created.addressId().toString()));

        mockMvc.perform(get("/api/v1/addresses/{addressId}", created.addressId()))
                .andExpect(status().isNotFound());
    }

/*
    ----------------------------------------
    --------REFERENTIAL INTEGRITY-----------
    ----------------------------------------
     */

    @Test
    void shouldReturn409WhenDeletingAddressReferencedByCustomer() throws Exception {
        AddressResponse address = createAddress(
                validAddressRequest(
                        "Referenced Address Street",
                        "99807",
                        "Referenced Address City"
                )
        );

        CustomerCreateRequest customerRequest = new CustomerCreateRequest(
                "CUST-ADDR-FK-9980",
                "Address FK Test Customer",
                "DE998000001",
                "address-fk@example.com",
                "+49 123 9980",
                "References address for delete test",
                true,
                address.addressId(),
                "TC40"
        );

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/v1/addresses/{addressId}", address.addressId()))
                .andExpect(status().isConflict());

        // The referenced address must still exist after the rejected delete.
        mockMvc.perform(get("/api/v1/addresses/{addressId}", address.addressId()))
                .andExpect(status().isOk());
    }
}
