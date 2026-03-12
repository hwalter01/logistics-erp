package com.logistics.masterdataservice.integration;

import com.logistics.masterdataservice.domain.enums.VehicleStatus;
import com.logistics.masterdataservice.dto.request.VehicleRequest;
import com.logistics.masterdataservice.dto.response.PagedResponse;
import com.logistics.masterdataservice.dto.response.VehicleResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VehicleControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private VehicleRequest validVehicleRequest(String vehicleNumber, String licensePlate) {
        return vehicleRequest(vehicleNumber, licensePlate, "Test Brand", "Test Model", VehicleStatus.ACTIVE);
    }

    private VehicleRequest vehicleRequest(
            String vehicleNumber,
            String licensePlate,
            String brand,
            String model,
            VehicleStatus status
    ) {
        return new VehicleRequest(
                vehicleNumber,
                licensePlate,
                "VIN9999",
                brand,
                model,
                status,
                "Test Notes"
        );
    }

    private void createVehicle(VehicleRequest request) throws Exception {
        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    private PagedResponse<VehicleResponse> searchVehicles(String filterName, String filterValue) throws Exception {
        String response = mockMvc.perform(get("/api/v1/vehicles")
                        .param(filterName, filterValue)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "vehicleNumber"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(
                response,
                new TypeReference<PagedResponse<VehicleResponse>>() {}
        );
    }

    private void assertVehiclePresent(PagedResponse<VehicleResponse> page, VehicleRequest expectedRequest) {
        VehicleResponse vehicle = page.content().stream()
                .filter(v -> expectedRequest.vehicleNumber().equals(v.vehicleNumber()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Vehicle not found: " + expectedRequest.vehicleNumber()));

        assertThat(vehicle.vehicleNumber()).isEqualTo(expectedRequest.vehicleNumber());
        assertThat(vehicle.licensePlate()).isEqualTo(expectedRequest.licensePlate());
        assertThat(vehicle.vin()).isEqualTo(expectedRequest.vin());
        assertThat(vehicle.brand()).isEqualTo(expectedRequest.brand());
        assertThat(vehicle.model()).isEqualTo(expectedRequest.model());
        assertThat(vehicle.status()).isEqualTo(expectedRequest.status());
        assertThat(vehicle.notes()).isEqualTo(expectedRequest.notes());
    }

    private void assertVehicleNotPresent(PagedResponse<VehicleResponse> page, String vehicleNumber) {
        assertThat(page.content().stream()
                .map(VehicleResponse::vehicleNumber))
                .doesNotContain(vehicleNumber);
    }

    /*
    ----------------------------------------
    ----------------Creating----------------
    ----------------------------------------
     */

    @Test
    void shouldCreateDriver() throws Exception {
        VehicleRequest vehicleRequest = validVehicleRequest("TRUCK-9901", "XX-XX-9999");

        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn409WhenVehicleNumberAlreadyExists() throws Exception {
        VehicleRequest vehicleRequest = validVehicleRequest("TRUCK-9902", "XX-XX-9999");
        VehicleRequest duplicateRequest = validVehicleRequest("TRUCK-9902", "XX-XX-9999");

        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict());
    }

    /*
    ----------------------------------------
    ----------------SEARCHING---------------
    ----------------------------------------
     */

    @Test
    void shouldSearchVehicleByVehicleNumber() throws Exception {
        VehicleRequest expectedVehicle = vehicleRequest("TRUCK-9903", "XX-VN-1003", "Brand-VN", "Model-VN", VehicleStatus.ACTIVE);
        VehicleRequest otherVehicle = vehicleRequest("TRUCK-9904", "XX-VN-1004", "Other-Brand", "Other-Model", VehicleStatus.INACTIVE);

        createVehicle(expectedVehicle);
        createVehicle(otherVehicle);

        PagedResponse<VehicleResponse> page = searchVehicles("vehicleNumber", expectedVehicle.vehicleNumber());

        assertThat(page.totalElements()).isEqualTo(1);
        assertVehiclePresent(page, expectedVehicle);
        assertVehicleNotPresent(page, otherVehicle.vehicleNumber());
    }

    @Test
    void shouldSearchVehicleByLicensePlate() throws Exception {
        VehicleRequest expectedVehicle = vehicleRequest("TRUCK-9905", "XX-LP-1005", "Brand-LP", "Model-LP", VehicleStatus.ACTIVE);
        VehicleRequest otherVehicle = vehicleRequest("TRUCK-9906", "XX-LP-1006", "Other-Brand", "Other-Model", VehicleStatus.INACTIVE);

        createVehicle(expectedVehicle);
        createVehicle(otherVehicle);

        PagedResponse<VehicleResponse> page = searchVehicles("licensePlate", expectedVehicle.licensePlate());

        assertVehiclePresent(page, expectedVehicle);
        assertVehicleNotPresent(page, otherVehicle.vehicleNumber());
    }

    @Test
    void shouldSearchVehicleByBrand() throws Exception {
        VehicleRequest expectedVehicle = vehicleRequest("TRUCK-9907", "XX-BR-1007", "Brand-Unique-Search", "Model-BR", VehicleStatus.ACTIVE);
        VehicleRequest otherVehicle = vehicleRequest("TRUCK-9908", "XX-BR-1008", "Different-Brand", "Other-Model", VehicleStatus.ACTIVE);

        createVehicle(expectedVehicle);
        createVehicle(otherVehicle);

        PagedResponse<VehicleResponse> page = searchVehicles("brand", expectedVehicle.brand());

        assertVehiclePresent(page, expectedVehicle);
        assertVehicleNotPresent(page, otherVehicle.vehicleNumber());
    }

    @Test
    void shouldSearchVehicleByModel() throws Exception {
        VehicleRequest expectedVehicle = vehicleRequest("TRUCK-9909", "XX-MO-1009", "Brand-MO", "Model-Unique-Search", VehicleStatus.ACTIVE);
        VehicleRequest otherVehicle = vehicleRequest("TRUCK-9910", "XX-MO-1010", "Other-Brand", "Different-Model", VehicleStatus.ACTIVE);

        createVehicle(expectedVehicle);
        createVehicle(otherVehicle);

        PagedResponse<VehicleResponse> page = searchVehicles("model", expectedVehicle.model());

        assertVehiclePresent(page, expectedVehicle);
        assertVehicleNotPresent(page, otherVehicle.vehicleNumber());
    }

    @Test
    void shouldSearchVehicleByStatus() throws Exception {
        VehicleRequest expectedVehicle = vehicleRequest("TRUCK-9911", "XX-ST-1011", "Brand-ST", "Model-ST", VehicleStatus.INACTIVE);
        VehicleRequest otherVehicle = vehicleRequest("TRUCK-9912", "XX-ST-1012", "Other-Brand", "Other-Model", VehicleStatus.ACTIVE);

        createVehicle(expectedVehicle);
        createVehicle(otherVehicle);

        PagedResponse<VehicleResponse> page = searchVehicles("status", expectedVehicle.status().name());

        assertVehiclePresent(page, expectedVehicle);
        assertVehicleNotPresent(page, otherVehicle.vehicleNumber());
    }


}
