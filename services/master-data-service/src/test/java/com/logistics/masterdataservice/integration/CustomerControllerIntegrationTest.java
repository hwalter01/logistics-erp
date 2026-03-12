package com.logistics.masterdataservice.integration;

import com.logistics.masterdataservice.dto.request.CustomerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private String addressId;

    @BeforeEach
    void setup() {
        addressId = "f9f9f9f9-9999-9999-9999-999999999901";
    }

    private CustomerRequest validCustomerRequest(String customerNumber, String name, String vatNumber, String addressId) {
        return new CustomerRequest(
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
        CustomerRequest request = validCustomerRequest("CUST-9991", "Automated Test Customer", "DE123456789", addressId);

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    //shouldReturn409WhenCustomerNumberAlreadyExists
    @Test
    void shouldReturn409WhenCustomerNumberAlreadyExists() throws Exception {
        CustomerRequest firstRequest = validCustomerRequest("CUST-9992", "Automated Test Customer", "DE123456789", addressId);
        CustomerRequest duplicateRequest = validCustomerRequest("CUST-9992", "Automated Test Customer 2", "DE123456789", addressId);

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
        CustomerRequest request = validCustomerRequest("CUST-9993", "Automated Test Customer", "DE123456789","f9f9f9f9-9999-9999-9999-999999999999");

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
        CustomerRequest request = validCustomerRequest("CUST-9994", "Customer number check", "DE123456789", addressId);

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
        CustomerRequest request = validCustomerRequest("CUST-9995", "Vat number check", "DE000000000", addressId);

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
        CustomerRequest request = validCustomerRequest("CUST-9996", "0000 name check", "DE123456789", addressId);

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
        CustomerRequest request = validCustomerRequest("CUST-9997", "0001 Pod required check", "DE123456789", addressId);

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());


        mockMvc.perform(get("/api/v1/customers")
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

    //customerNumber, vatNumber, name, podRequired
    @Test
    void shouldCombineCustomerFilters() throws Exception {
        CustomerRequest request = validCustomerRequest("CUST-9998", "0003 combined check", "DE123456789",addressId);

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
}
