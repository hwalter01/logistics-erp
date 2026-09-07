package com.logistics.masterdataservice.integration;

import com.logistics.masterdataservice.domain.enums.TrailerStatus;
import com.logistics.masterdataservice.domain.enums.TrailerType;
import com.logistics.masterdataservice.dto.request.createrequest.TrailerCreateRequest;
import com.logistics.masterdataservice.dto.request.updaterequest.TrailerUpdateRequest;
import com.logistics.masterdataservice.dto.response.PagedResponse;
import com.logistics.masterdataservice.dto.response.TrailerResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TrailerControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private TrailerCreateRequest validTrailerCreateRequest(
            String trailerNumber,
            String licensePlate
    ) {
        return trailerCreateRequest(
                trailerNumber,
                licensePlate,
                TrailerType.CURTAINSIDER,
                TrailerStatus.ACTIVE
        );
    }

    private TrailerCreateRequest trailerCreateRequest(
            String trailerNumber,
            String licensePlate,
            TrailerType trailerType,
            TrailerStatus status
    ) {
        return new TrailerCreateRequest(
                trailerNumber,
                licensePlate,
                trailerType,
                status,
                "Automated test trailer"
        );
    }

    private void createTrailer(TrailerCreateRequest request) throws Exception {
        mockMvc.perform(post("/api/v1/trailers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    private PagedResponse<TrailerResponse> searchTrailers(
            String filterName,
            String filterValue
    ) throws Exception {
        String response = mockMvc.perform(get("/api/v1/trailers")
                        .param(filterName, filterValue)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "trailerNumber"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(
                response,
                new TypeReference<PagedResponse<TrailerResponse>>() {}
        );
    }

    private void assertTrailerPresent(
            PagedResponse<TrailerResponse> page,
            TrailerCreateRequest expectedRequest
    ) {
        TrailerResponse trailer = page.content().stream()
                .filter(t -> expectedRequest.trailerNumber().equals(t.trailerNumber()))
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError("Trailer not found: " + expectedRequest.trailerNumber()));

        assertThat(trailer.trailerNumber()).isEqualTo(expectedRequest.trailerNumber());
        assertThat(trailer.licensePlate()).isEqualTo(expectedRequest.licensePlate());
        assertThat(trailer.trailerType()).isEqualTo(expectedRequest.trailerType());
        assertThat(trailer.status()).isEqualTo(expectedRequest.status());
        assertThat(trailer.notes()).isEqualTo(expectedRequest.notes());
    }

    private void assertTrailerNotPresent(
            PagedResponse<TrailerResponse> page,
            String trailerNumber
    ) {
        assertThat(page.content().stream()
                .map(TrailerResponse::trailerNumber))
                .doesNotContain(trailerNumber);
    }

    /*
    ----------------------------------------
    ----------------Creating----------------
    ----------------------------------------
     */

    @Test
    void shouldCreateTrailer() throws Exception {
        TrailerCreateRequest request =
                validTrailerCreateRequest("TRL-9901", "XX-TR-9901");

        createTrailer(request);
    }

    @Test
    void shouldReturn409WhenTrailerNumberAlreadyExists() throws Exception {
        TrailerCreateRequest firstRequest =
                validTrailerCreateRequest("TRL-9902", "XX-TR-9902");

        TrailerCreateRequest duplicateRequest =
                validTrailerCreateRequest("TRL-9902", "XX-TR-9903");

        createTrailer(firstRequest);

        mockMvc.perform(post("/api/v1/trailers")
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
    void shouldSearchTrailerByTrailerNumber() throws Exception {
        TrailerCreateRequest expectedTrailer = trailerCreateRequest(
                "TRL-9903",
                "XX-NR-9903",
                TrailerType.CURTAINSIDER,
                TrailerStatus.ACTIVE
        );

        TrailerCreateRequest otherTrailer = trailerCreateRequest(
                "TRL-9904",
                "XX-NR-9904",
                TrailerType.REEFER,
                TrailerStatus.INACTIVE
        );

        createTrailer(expectedTrailer);
        createTrailer(otherTrailer);

        PagedResponse<TrailerResponse> page =
                searchTrailers("trailerNumber", expectedTrailer.trailerNumber());

        assertThat(page.totalElements()).isEqualTo(1);
        assertTrailerPresent(page, expectedTrailer);
        assertTrailerNotPresent(page, otherTrailer.trailerNumber());
    }

    @Test
    void shouldSearchTrailerByLicensePlate() throws Exception {
        TrailerCreateRequest expectedTrailer = trailerCreateRequest(
                "TRL-9905",
                "XX-LP-UNIQUE-9905",
                TrailerType.BOX,
                TrailerStatus.ACTIVE
        );

        TrailerCreateRequest otherTrailer = trailerCreateRequest(
                "TRL-9906",
                "XX-LP-9906",
                TrailerType.BOX,
                TrailerStatus.ACTIVE
        );

        createTrailer(expectedTrailer);
        createTrailer(otherTrailer);

        PagedResponse<TrailerResponse> page =
                searchTrailers("licensePlate", expectedTrailer.licensePlate());

        assertTrailerPresent(page, expectedTrailer);
        assertTrailerNotPresent(page, otherTrailer.trailerNumber());
    }

    @Test
    void shouldSearchTrailerByTrailerType() throws Exception {
        TrailerCreateRequest expectedTrailer = trailerCreateRequest(
                "TRL-9907",
                "XX-TY-9907",
                TrailerType.TANK,
                TrailerStatus.ACTIVE
        );

        TrailerCreateRequest otherTrailer = trailerCreateRequest(
                "TRL-9908",
                "XX-TY-9908",
                TrailerType.FLATBED,
                TrailerStatus.ACTIVE
        );

        createTrailer(expectedTrailer);
        createTrailer(otherTrailer);

        PagedResponse<TrailerResponse> page =
                searchTrailers("trailerType", expectedTrailer.trailerType().name());

        assertTrailerPresent(page, expectedTrailer);
        assertTrailerNotPresent(page, otherTrailer.trailerNumber());
    }

    @Test
    void shouldSearchTrailerByStatus() throws Exception {
        TrailerCreateRequest expectedTrailer = trailerCreateRequest(
                "TRL-9909",
                "XX-ST-9909",
                TrailerType.REEFER,
                TrailerStatus.MAINTENANCE
        );

        TrailerCreateRequest otherTrailer = trailerCreateRequest(
                "TRL-9910",
                "XX-ST-9910",
                TrailerType.REEFER,
                TrailerStatus.ACTIVE
        );

        createTrailer(expectedTrailer);
        createTrailer(otherTrailer);

        PagedResponse<TrailerResponse> page =
                searchTrailers("status", expectedTrailer.status().name());

        assertTrailerPresent(page, expectedTrailer);
        assertTrailerNotPresent(page, otherTrailer.trailerNumber());
    }

    @Test
    void shouldCombineTrailerFilters() throws Exception {
        TrailerCreateRequest expectedTrailer = trailerCreateRequest(
                "TRL-9911",
                "XX-COMB-9911",
                TrailerType.CURTAINSIDER,
                TrailerStatus.ACTIVE
        );

        TrailerCreateRequest otherTrailer = trailerCreateRequest(
                "TRL-9912",
                "XX-COMB-9912",
                TrailerType.CURTAINSIDER,
                TrailerStatus.INACTIVE
        );

        createTrailer(expectedTrailer);
        createTrailer(otherTrailer);

        String response = mockMvc.perform(get("/api/v1/trailers")
                        .param("trailerNumber", expectedTrailer.trailerNumber())
                        .param("licensePlate", expectedTrailer.licensePlate())
                        .param("trailerType", expectedTrailer.trailerType().name())
                        .param("status", expectedTrailer.status().name())
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "trailerNumber"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PagedResponse<TrailerResponse> page = objectMapper.readValue(
                response,
                new TypeReference<PagedResponse<TrailerResponse>>() {}
        );

        assertThat(page.totalElements()).isEqualTo(1);
        assertTrailerPresent(page, expectedTrailer);
        assertTrailerNotPresent(page, otherTrailer.trailerNumber());
    }

    /*
    ----------------------------------------
    ----------------UPDATING----------------
    ----------------------------------------
     */

    @Test
    void shouldUpdateTrailerWithoutChangingTrailerNumber() throws Exception {
        TrailerCreateRequest createRequest =
                validTrailerCreateRequest("TRL-9980", "XX-UP-9980");

        String createResponse = mockMvc.perform(post("/api/v1/trailers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        TrailerResponse createdTrailer =
                objectMapper.readValue(createResponse, TrailerResponse.class);

        TrailerUpdateRequest updateRequest = new TrailerUpdateRequest(
                "XX-UP-9981",
                TrailerType.REEFER,
                TrailerStatus.MAINTENANCE,
                "Updated trailer notes"
        );

        mockMvc.perform(put("/api/v1/trailers/{trailerId}", createdTrailer.trailerId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        PagedResponse<TrailerResponse> page =
                searchTrailers("trailerNumber", "TRL-9980");

        TrailerResponse updatedTrailer = page.content().stream()
                .filter(t -> "TRL-9980".equals(t.trailerNumber()))
                .findFirst()
                .orElseThrow();

        assertThat(updatedTrailer.trailerNumber()).isEqualTo("TRL-9980");
        assertThat(updatedTrailer.licensePlate()).isEqualTo("XX-UP-9981");
        assertThat(updatedTrailer.trailerType()).isEqualTo(TrailerType.REEFER);
        assertThat(updatedTrailer.status()).isEqualTo(TrailerStatus.MAINTENANCE);
        assertThat(updatedTrailer.notes()).isEqualTo("Updated trailer notes");
    }
}
