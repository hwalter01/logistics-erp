package com.logistics.masterdataservice.integration;

import com.logistics.masterdataservice.domain.enums.DriverStatus;
import com.logistics.masterdataservice.domain.enums.EmploymentType;
import com.logistics.masterdataservice.dto.request.AddressRequest;
import com.logistics.masterdataservice.dto.request.createrequest.DriverCreateRequest;
import com.logistics.masterdataservice.dto.request.updaterequest.DriverUpdateRequest;
import com.logistics.masterdataservice.dto.response.AddressResponse;
import com.logistics.masterdataservice.dto.response.DriverResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
public class DriverControllerIntegrationTest extends AbstractIntegrationTest{

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private String addressId;

    @BeforeEach
    void setup() throws Exception {
        AddressRequest addressRequest = new AddressRequest(
                "Driver Test Street",
                "1",
                "99002",
                "Driver Test City",
                "Germany",
                "Created by driver integration test"
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

    private DriverCreateRequest validDriverCreateRequest(String driverNumber, String firstName, String lastName, String addressId) {
        return new DriverCreateRequest(
                driverNumber,
                firstName,
                lastName,
                "+49 123 4567",
                "test@example.com",
                "99999-99999-99999",
                EmploymentType.EMPLOYEE,
                DriverStatus.ACTIVE,
                UUID.fromString(addressId)
        );
    }

    /*
    ----------------------------------------
    ----------------Creating----------------
    ----------------------------------------
     */

    @Test
    void shouldCreateDriver() throws Exception {
        DriverCreateRequest driverRequest = validDriverCreateRequest("DRV-9901", "Automated Test Driver First Name", "Automated Test Driver Last Name", addressId);

        mockMvc.perform(post("/api/v1/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn409WhenDriverNumberAlreadyExists() throws Exception {
        DriverCreateRequest firstRequest = validDriverCreateRequest("DRV-9902", "Automated Test Driver First Name", "Automated Test Driver Last Name", addressId);
        DriverCreateRequest duplicateRequest = validDriverCreateRequest("DRV-9902", "Automated Test Driver First Name", "Automated Test Driver Last Name", addressId);

        mockMvc.perform(post("/api/v1/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturn404WhenAddressDoesNotExists() throws Exception {
        DriverCreateRequest driverRequest = validDriverCreateRequest("DRV-9903", "Automated Test Driver First Name", "Automated Test Driver Last Name","f9f9f9f9-9999-9999-9999-999999999999");

        mockMvc.perform(post("/api/v1/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverRequest)))
                .andExpect(status().isNotFound());
    }

    /*
    ----------------------------------------
    ----------------SEARCHING---------------
    ----------------------------------------
     */

    //driverNumber
    @Test
    void shouldSearchDriverByDriverNumber() throws Exception {
        DriverCreateRequest driverRequest = validDriverCreateRequest("DRV-9904", "Automated Test Driver First Name", "Automated Test Driver Last Name", addressId);

        mockMvc.perform(post("/api/v1/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverRequest)))
                .andExpect(status().isOk());


        mockMvc.perform(get("/api/v1/drivers")
                        .param("driverNumber", driverRequest.driverNumber())
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "driverNumber"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.content[0].driverNumber").value(driverRequest.driverNumber()))
                .andExpect(jsonPath("$.content[0].firstName").value(driverRequest.firstName()))
                .andExpect(jsonPath("$.content[0].lastName").value(driverRequest.lastName()))
                .andExpect(jsonPath("$.content[0].phone").value(driverRequest.phone()))
                .andExpect(jsonPath("$.content[0].email").value(driverRequest.email()))
                .andExpect(jsonPath("$.content[0].licenseNumber").value(driverRequest.licenseNumber()))
                .andExpect(jsonPath("$.content[0].employmentType").value(String.valueOf(driverRequest.employmentType())))
                .andExpect(jsonPath("$.content[0].status").value(String.valueOf(driverRequest.status())))
                .andExpect(jsonPath("$.content[0].addressId").value(String.valueOf(driverRequest.addressId())));

    }

    //firstName
    @Test
    void shouldSearchDriverByFirstName() throws Exception {
        DriverCreateRequest driverRequest = validDriverCreateRequest("DRV-9905", "0000 Automated Test Driver First Name", "Automated Test Driver Last Name", addressId);

        mockMvc.perform(post("/api/v1/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverRequest)))
                .andExpect(status().isOk());


        mockMvc.perform(get("/api/v1/drivers")
                        .param("firstName", driverRequest.firstName())
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "firstName"))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.content.length()").value(1)) //These are for unique matches, first name is partial match
                //.andExpect(jsonPath("$.totalElements").value(1))
                //.andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.content[0].driverNumber").value(driverRequest.driverNumber()))
                .andExpect(jsonPath("$.content[0].firstName").value(driverRequest.firstName()))
                .andExpect(jsonPath("$.content[0].lastName").value(driverRequest.lastName()))
                .andExpect(jsonPath("$.content[0].phone").value(driverRequest.phone()))
                .andExpect(jsonPath("$.content[0].email").value(driverRequest.email()))
                .andExpect(jsonPath("$.content[0].licenseNumber").value(driverRequest.licenseNumber()))
                .andExpect(jsonPath("$.content[0].employmentType").value(String.valueOf(driverRequest.employmentType())))
                .andExpect(jsonPath("$.content[0].status").value(String.valueOf(driverRequest.status())))
                .andExpect(jsonPath("$.content[0].addressId").value(String.valueOf(driverRequest.addressId())));

    }

    //lastName
    @Test
    void shouldSearchDriverByLastName() throws Exception {
        DriverCreateRequest driverRequest = validDriverCreateRequest("DRV-9906", "Automated Test Driver First Name", "0000 Automated Test Driver Last Name", addressId);

        mockMvc.perform(post("/api/v1/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverRequest)))
                .andExpect(status().isOk());


        mockMvc.perform(get("/api/v1/drivers")
                        .param("lastName", driverRequest.lastName())
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "lastName"))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.content.length()").value(1)) //These are for unique matches, last name is partial match
                //.andExpect(jsonPath("$.totalElements").value(1))
                //.andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.content[0].driverNumber").value(driverRequest.driverNumber()))
                .andExpect(jsonPath("$.content[0].firstName").value(driverRequest.firstName()))
                .andExpect(jsonPath("$.content[0].lastName").value(driverRequest.lastName()))
                .andExpect(jsonPath("$.content[0].phone").value(driverRequest.phone()))
                .andExpect(jsonPath("$.content[0].email").value(driverRequest.email()))
                .andExpect(jsonPath("$.content[0].licenseNumber").value(driverRequest.licenseNumber()))
                .andExpect(jsonPath("$.content[0].employmentType").value(String.valueOf(driverRequest.employmentType())))
                .andExpect(jsonPath("$.content[0].status").value(String.valueOf(driverRequest.status())))
                .andExpect(jsonPath("$.content[0].addressId").value(String.valueOf(driverRequest.addressId())));

    }

    //employmentType
    @Test
    void shouldSearchDriverByEmploymentType() throws Exception {
        DriverCreateRequest driverRequest = validDriverCreateRequest("DRV-0907", "Automated Test Driver First Name", "Automated Test Driver Last Name", addressId);

        mockMvc.perform(post("/api/v1/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverRequest)))
                .andExpect(status().isOk());


        mockMvc.perform(get("/api/v1/drivers")
                        .param("employmentType", String.valueOf(driverRequest.employmentType()))
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "driverNumber"))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.content.length()").value(1)) //These are for unique matches, EmploymentType is not unique
                //.andExpect(jsonPath("$.totalElements").value(1))
                //.andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.content[0].driverNumber").value(driverRequest.driverNumber()))
                .andExpect(jsonPath("$.content[0].firstName").value(driverRequest.firstName()))
                .andExpect(jsonPath("$.content[0].lastName").value(driverRequest.lastName()))
                .andExpect(jsonPath("$.content[0].phone").value(driverRequest.phone()))
                .andExpect(jsonPath("$.content[0].email").value(driverRequest.email()))
                .andExpect(jsonPath("$.content[0].licenseNumber").value(driverRequest.licenseNumber()))
                .andExpect(jsonPath("$.content[0].employmentType").value(String.valueOf(driverRequest.employmentType())))
                .andExpect(jsonPath("$.content[0].status").value(String.valueOf(driverRequest.status())))
                .andExpect(jsonPath("$.content[0].addressId").value(String.valueOf(driverRequest.addressId())));

    }

    //status
    @Test
    void shouldSearchDriverByStatus() throws Exception {
        DriverCreateRequest driverRequest = validDriverCreateRequest("DRV-0908", "Automated Test Driver First Name", "Automated Test Driver Last Name", addressId);

        mockMvc.perform(post("/api/v1/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverRequest)))
                .andExpect(status().isOk());


        mockMvc.perform(get("/api/v1/drivers")
                        .param("status", String.valueOf(driverRequest.status()))
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "driverNumber"))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.content.length()").value(1)) //These are for unique matches, EmploymentType is not unique
                //.andExpect(jsonPath("$.totalElements").value(1))
                //.andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.content[0].driverNumber").value(driverRequest.driverNumber()))
                .andExpect(jsonPath("$.content[0].firstName").value(driverRequest.firstName()))
                .andExpect(jsonPath("$.content[0].lastName").value(driverRequest.lastName()))
                .andExpect(jsonPath("$.content[0].phone").value(driverRequest.phone()))
                .andExpect(jsonPath("$.content[0].email").value(driverRequest.email()))
                .andExpect(jsonPath("$.content[0].licenseNumber").value(driverRequest.licenseNumber()))
                .andExpect(jsonPath("$.content[0].employmentType").value(String.valueOf(driverRequest.employmentType())))
                .andExpect(jsonPath("$.content[0].status").value(String.valueOf(driverRequest.status())))
                .andExpect(jsonPath("$.content[0].addressId").value(String.valueOf(driverRequest.addressId())));
    }

    //driverNumber, firstName, lastName, employmentType, status
    @Test
    void shouldCombineDriverFilters() throws Exception{
        DriverCreateRequest driverRequest = validDriverCreateRequest("DRV-9909", "Automated Test Driver First Name", "Automated Test Driver Last Name", addressId);

        mockMvc.perform(post("/api/v1/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverRequest)))
                .andExpect(status().isOk());


        mockMvc.perform(get("/api/v1/drivers")
                        .param("driverNumber", driverRequest.driverNumber())
                        .param("firstName", driverRequest.firstName())
                        .param("lastName", driverRequest.lastName())
                        .param("status", String.valueOf(driverRequest.status()))
                        .param("employmentType", String.valueOf(driverRequest.employmentType()))
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "driverNumber"))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.content.length()").value(1)) //These are for unique matches, EmploymentType is not unique
                //.andExpect(jsonPath("$.totalElements").value(1))
                //.andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.content[0].driverNumber").value(driverRequest.driverNumber()))
                .andExpect(jsonPath("$.content[0].firstName").value(driverRequest.firstName()))
                .andExpect(jsonPath("$.content[0].lastName").value(driverRequest.lastName()))
                .andExpect(jsonPath("$.content[0].phone").value(driverRequest.phone()))
                .andExpect(jsonPath("$.content[0].email").value(driverRequest.email()))
                .andExpect(jsonPath("$.content[0].licenseNumber").value(driverRequest.licenseNumber()))
                .andExpect(jsonPath("$.content[0].employmentType").value(String.valueOf(driverRequest.employmentType())))
                .andExpect(jsonPath("$.content[0].status").value(String.valueOf(driverRequest.status())))
                .andExpect(jsonPath("$.content[0].addressId").value(String.valueOf(driverRequest.addressId())));
    }


    /*
    ----------------------------------------
    ----------------UPDATING----------------
    ----------------------------------------
     */

    @Test
    void shouldUpdateDriverWithoutChangingDriverNumber() throws Exception {
        DriverCreateRequest createRequest = validDriverCreateRequest(
                "DRV-0980",
                "Original",
                "Driver",
                addressId
        );

        String createResponse = mockMvc.perform(post("/api/v1/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        DriverResponse createdDriver =
                objectMapper.readValue(createResponse, DriverResponse.class);

        DriverUpdateRequest updateRequest = new DriverUpdateRequest(
                "Updated",
                "Driver",
                "+49 170 9999999",
                "updated.driver@example.com",
                "B99999999",
                EmploymentType.EMPLOYEE,
                DriverStatus.ACTIVE,
                UUID.fromString(addressId)
        );

        mockMvc.perform(put("/api/v1/drivers/{driverId}", createdDriver.driverId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.driverNumber").value("DRV-0980"))
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("Driver"))
                .andExpect(jsonPath("$.phone").value("+49 170 9999999"))
                .andExpect(jsonPath("$.email").value("updated.driver@example.com"))
                .andExpect(jsonPath("$.licenseNumber").value("B99999999"))
                .andExpect(jsonPath("$.employmentType").value("EMPLOYEE"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

/*
    ----------------------------------------
    ----------------VALIDATION--------------
    ----------------------------------------
     */

    @Test
    void shouldReturn400ForInvalidDriver() throws Exception {
        DriverCreateRequest request = new DriverCreateRequest(
                "",
                "",
                "",
                "+49 170 1234567",
                "driver@example.com",
                "",
                null,
                null,
                null
        );

        mockMvc.perform(post("/api/v1/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
