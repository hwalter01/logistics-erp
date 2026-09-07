package com.logistics.masterdataservice.integration;

import com.logistics.masterdataservice.dto.request.AddressRequest;
import com.logistics.masterdataservice.dto.request.createrequest.CustomerCreateRequest;
import com.logistics.masterdataservice.dto.request.updaterequest.CustomerUpdateRequest;
import com.logistics.masterdataservice.dto.response.AddressResponse;
import com.logistics.masterdataservice.dto.response.CustomerResponse;
import com.logistics.masterdataservice.dto.response.PagedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private String addressId;

    @BeforeEach
    void setup() throws Exception {
        AddressRequest addressRequest = new AddressRequest(
                "Customer Test Street",
                "1",
                "99001",
                "Customer Test City",
                "Germany",
                "Created by integration test"
        );

        String response = mockMvc.perform(post("/api/v1/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AddressResponse addressResponse =
                objectMapper.readValue(response, AddressResponse.class);

        addressId = addressResponse.addressId().toString();
    }

    private CustomerCreateRequest validCustomerCreateRequest(String customerNumber, String name, String vatNumber, String addressId) {
        return new CustomerCreateRequest(
                customerNumber,
                name,
                vatNumber,
                "test@example.com",
                "+49 123 4567",
                "notes",
                true,
                UUID.fromString(addressId)
        );
    }

    /*
    ----------------------------------------
    ----------------Creating----------------
    ----------------------------------------
     */

    //shouldCreateCustomer
    @Test
    void shouldCreateCustomer() throws Exception {
        CustomerCreateRequest request = validCustomerCreateRequest("CUST-9991", "Automated Test Customer", "DE123456789", addressId);

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    //shouldReturn409WhenCustomerNumberAlreadyExists
    @Test
    void shouldReturn409WhenCustomerNumberAlreadyExists() throws Exception {
        CustomerCreateRequest firstRequest = validCustomerCreateRequest("CUST-9992", "Automated Test Customer", "DE123456789", addressId);
        CustomerCreateRequest duplicateRequest = validCustomerCreateRequest("CUST-9992", "Automated Test Customer 2", "DE123456789", addressId);

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict());
    }

    //shouldReturn404WhenAddressDoesNotExist
    @Test
    void shouldReturn404WhenAddressDoesNotExist() throws Exception {
        CustomerCreateRequest request = validCustomerCreateRequest("CUST-9993", "Automated Test Customer", "DE123456789","f9f9f9f9-9999-9999-9999-999999999999");

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }


    /*
    ----------------------------------------
    ----------------SEARCHING---------------
    ----------------------------------------
     */

    //customerNumber
    @Test
    void shouldSearchCustomerByCustomerNumber() throws Exception {
        CustomerCreateRequest request = validCustomerCreateRequest("CUST-9994", "Customer number check", "DE123456789", addressId);

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());


        mockMvc.perform(get("/api/v1/customers")
                        .param("customerNumber", request.customerNumber())
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.content[0].customerNumber").value(request.customerNumber()))
                .andExpect(jsonPath("$.content[0].name").value(request.name()))
                .andExpect(jsonPath("$.content[0].vatNumber").value(request.vatNumber()))
                .andExpect(jsonPath("$.content[0].contactEmail").value(request.contactEmail()))
                .andExpect(jsonPath("$.content[0].contactPhone").value(request.contactPhone()))
                .andExpect(jsonPath("$.content[0].notes").value(request.notes()))
                .andExpect(jsonPath("$.content[0].podRequired").value(request.podRequired()))
                .andExpect(jsonPath("$.content[0].addressId").value(request.addressId().toString()));
    }

    //vatNumber
    @Test
    void shouldSearchCustomerByVatNumber() throws Exception {
        CustomerCreateRequest request = validCustomerCreateRequest("CUST-9995", "Vat number check", "DE000000000", addressId);

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());


        mockMvc.perform(get("/api/v1/customers")
                        .param("vatNumber", request.vatNumber())
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "vatNumber"))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.content.length()").value(1))
                //.andExpect(jsonPath("$.totalElements").value(1))
                //.andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.content[0].customerNumber").value(request.customerNumber()))
                .andExpect(jsonPath("$.content[0].name").value(request.name()))
                .andExpect(jsonPath("$.content[0].vatNumber").value(request.vatNumber()))
                .andExpect(jsonPath("$.content[0].contactEmail").value(request.contactEmail()))
                .andExpect(jsonPath("$.content[0].contactPhone").value(request.contactPhone()))
                .andExpect(jsonPath("$.content[0].notes").value(request.notes()))
                .andExpect(jsonPath("$.content[0].podRequired").value(request.podRequired()))
                .andExpect(jsonPath("$.content[0].addressId").value(request.addressId().toString()));
    }

    //name
    @Test
    void shouldSearchCustomerByName() throws Exception {
        CustomerCreateRequest request = validCustomerCreateRequest("CUST-9996", "0000 name check", "DE123456789", addressId);

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());


        mockMvc.perform(get("/api/v1/customers")
                        .param("name", request.name())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.content[0].customerNumber").value(request.customerNumber()))
                .andExpect(jsonPath("$.content[0].name").value(request.name()))
                .andExpect(jsonPath("$.content[0].vatNumber").value(request.vatNumber()))
                .andExpect(jsonPath("$.content[0].contactEmail").value(request.contactEmail()))
                .andExpect(jsonPath("$.content[0].contactPhone").value(request.contactPhone()))
                .andExpect(jsonPath("$.content[0].notes").value(request.notes()))
                .andExpect(jsonPath("$.content[0].podRequired").value(request.podRequired()))
                .andExpect(jsonPath("$.content[0].addressId").value(request.addressId().toString()));
    }

    //podRequired
    @Test
    void shouldSearchCustomerByPodRequired() throws Exception {
        CustomerCreateRequest podRequiredCustomer =
                validCustomerCreateRequest(
                        "CUST-POD-TRUE-9997",
                        "POD Required Customer",
                        "DE123456789",
                        addressId
                );

        CustomerCreateRequest noPodCustomer =
                new CustomerCreateRequest(
                        "CUST-POD-FALSE-9997",
                        "No POD Customer",
                        "DE987654321",
                        "nopod@example.com",
                        "+49 123456",
                        "No POD",
                        false,
                        UUID.fromString(addressId)
                );

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(podRequiredCustomer)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noPodCustomer)))
                .andExpect(status().isOk());

        String response = mockMvc.perform(get("/api/v1/customers")
                        .param("podRequired", "true")
                        .param("page", "0")
                        .param("size", "100"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PagedResponse<CustomerResponse> page = objectMapper.readValue(
                response,
                new TypeReference<PagedResponse<CustomerResponse>>() {}
        );

        assertThat(
                page.content().stream()
                        .map(CustomerResponse::customerNumber)
        )
                .contains("CUST-POD-TRUE-9997")
                .doesNotContain("CUST-POD-FALSE-9997");

        assertThat(
                page.content()
                        .stream()
                        .allMatch(CustomerResponse::podRequired)
        ).isTrue();
    }

    //customerNumber, vatNumber, name, podRequired
    @Test
    void shouldCombineCustomerFilters() throws Exception {
        CustomerCreateRequest request = validCustomerCreateRequest("CUST-9998", "0003 combined check", "DE123456789",addressId);

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());


        mockMvc.perform(get("/api/v1/customers")
                        .param("customerNumber", request.customerNumber())
                        .param("name", request.name())
                        .param("podRequired", request.podRequired().toString())
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.content[0].customerNumber").value(request.customerNumber()))
                .andExpect(jsonPath("$.content[0].name").value(request.name()))
                .andExpect(jsonPath("$.content[0].vatNumber").value(request.vatNumber()))
                .andExpect(jsonPath("$.content[0].contactEmail").value(request.contactEmail()))
                .andExpect(jsonPath("$.content[0].contactPhone").value(request.contactPhone()))
                .andExpect(jsonPath("$.content[0].notes").value(request.notes()))
                .andExpect(jsonPath("$.content[0].podRequired").value(request.podRequired()))
                .andExpect(jsonPath("$.content[0].addressId").value(request.addressId().toString()));
    }


    /*
    ----------------------------------------
    ----------------UPDATING----------------
    ----------------------------------------
     */

    @Test
    void shouldUpdateCustomerWithoutChangingCustomerNumber() throws Exception {
        CustomerCreateRequest createRequest = validCustomerCreateRequest(
                "CUST-9980",
                "Original Customer",
                "DE111111111",
                addressId
        );

        String createResponse = mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CustomerResponse createdCustomer =
                objectMapper.readValue(createResponse, CustomerResponse.class);

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "Updated Customer",
                "DE222222222",
                "updated@example.com",
                "+49 999 9999",
                "updated notes",
                false,
                UUID.fromString(addressId)
        );

        mockMvc.perform(put("/api/v1/customers/{customerId}", createdCustomer.customerId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerNumber").value("CUST-9980"))
                .andExpect(jsonPath("$.name").value("Updated Customer"))
                .andExpect(jsonPath("$.vatNumber").value("DE222222222"))
                .andExpect(jsonPath("$.contactEmail").value("updated@example.com"))
                .andExpect(jsonPath("$.contactPhone").value("+49 999 9999"))
                .andExpect(jsonPath("$.notes").value("updated notes"))
                .andExpect(jsonPath("$.podRequired").value(false));
    }

/*
    ----------------------------------------
    ----------------VALIDATION--------------
    ----------------------------------------
     */

    @Test
    void shouldReturn400ForInvalidCustomer() throws Exception {
        CustomerCreateRequest request = new CustomerCreateRequest(
                "",
                "",
                "DE123456789",
                "test@example.com",
                "+49 123 4567",
                "invalid customer test",
                null,
                null
        );

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
