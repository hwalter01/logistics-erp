package com.logistics.masterdataservice.integration;

import com.logistics.masterdataservice.domain.enums.DriverStatus;
import com.logistics.masterdataservice.domain.enums.EmploymentType;
import com.logistics.masterdataservice.dto.request.DriverRequest;
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

@SpringBootTest
@AutoConfigureMockMvc
public class DriverControllerIntegrationTest extends AbstractIntegrationTest{

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private String addressId;

    @BeforeEach
    public void setup() {
        addressId = "f9f9f9f9-9999-9999-9999-999999999901";
    }

    private DriverRequest validDriverRequest(String driverNumber, String firstName, String lastName, String addressId) {
        return new DriverRequest(
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
        DriverRequest driverRequest = validDriverRequest("DRV-9901", "Automated Test Driver First Name", "Automated Test Driver Last Name", addressId);

        mockMvc.perform(post("/api/v1/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn409WhenDriverNumberAlreadyExists() throws Exception {
        DriverRequest firstRequest = validDriverRequest("DRV-9902", "Automated Test Driver First Name", "Automated Test Driver Last Name", addressId);
        DriverRequest duplicateRequest = validDriverRequest("DRV-9902", "Automated Test Driver First Name", "Automated Test Driver Last Name", addressId);

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
        DriverRequest driverRequest = validDriverRequest("DRV-9903", "Automated Test Driver First Name", "Automated Test Driver Last Name","f9f9f9f9-9999-9999-9999-999999999999");

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
        DriverRequest driverRequest = validDriverRequest("DRV-9904", "Automated Test Driver First Name", "Automated Test Driver Last Name", addressId);

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
        DriverRequest driverRequest = validDriverRequest("DRV-9905", "0000 Automated Test Driver First Name", "Automated Test Driver Last Name", addressId);

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
        DriverRequest driverRequest = validDriverRequest("DRV-9906", "Automated Test Driver First Name", "0000 Automated Test Driver Last Name", addressId);

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
        DriverRequest driverRequest = validDriverRequest("DRV-0907", "Automated Test Driver First Name", "Automated Test Driver Last Name", addressId);

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
        DriverRequest driverRequest = validDriverRequest("DRV-0908", "Automated Test Driver First Name", "Automated Test Driver Last Name", addressId);

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
        DriverRequest driverRequest = validDriverRequest("DRV-9909", "Automated Test Driver First Name", "Automated Test Driver Last Name", addressId);

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
}
