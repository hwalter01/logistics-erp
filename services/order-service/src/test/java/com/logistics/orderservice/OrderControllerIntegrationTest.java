package com.logistics.orderservice;

import com.logistics.orderservice.client.masterdata.MasterDataClient;
import com.logistics.orderservice.client.masterdata.MasterDataCustomerResponse;
import com.logistics.orderservice.client.masterdata.MasterDataLocationResponse;
import com.logistics.orderservice.exceptions.MasterDataNotFoundException;
import com.logistics.orderservice.exceptions.MasterDataUnavailableException;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MasterDataClient masterDataClient;


    // ---------------------------------------------------------
    // 201 - Happy Path
    // ---------------------------------------------------------

    @Test
    void createOrder_shouldReturn201() throws Exception {

        UUID customerId = UUID.randomUUID();
        UUID pickupLocationId = UUID.randomUUID();
        UUID deliveryLocationId = UUID.randomUUID();

        mockMasterData(
                customerId,
                randomShortCode(),
                pickupLocationId,
                deliveryLocationId
        );

        String requestBody = validRequest(
                customerId,
                pickupLocationId,
                deliveryLocationId,
                "PO-" + UUID.randomUUID()
        );

        mockMvc.perform(
                        post("/api/v1/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").exists())
                .andExpect(jsonPath("$.orderNumber").exists())
                .andExpect(jsonPath("$.customerId")
                        .value(customerId.toString()))
                .andExpect(jsonPath("$.status")
                        .value("DRAFT"))
                .andExpect(jsonPath("$.orderStops.length()")
                        .value(2))
                .andExpect(jsonPath("$.cargoItems.length()")
                        .value(1));
    }


    // ---------------------------------------------------------
    // 400 - PICKUP + DELIVERY required
    // ---------------------------------------------------------

    @Test
    void createOrder_withoutDelivery_shouldReturn400() throws Exception {

        UUID customerId = UUID.randomUUID();
        UUID location1 = UUID.randomUUID();
        UUID location2 = UUID.randomUUID();

        String requestBody = """
                {
                  "customerId": "%s",
                  "customerReference": "PO-%s",
                  "orderStops": [
                    {
                      "sequenceNumber": 1,
                      "type": "PICKUP",
                      "locationId": "%s"
                    },
                    {
                      "sequenceNumber": 2,
                      "type": "PICKUP",
                      "locationId": "%s"
                    }
                  ],
                  "cargoItems": [
                    {
                      "description": "Steel coils",
                      "measurements": [
                        {
                          "value": 1000,
                          "unit": "KILOGRAM"
                        }
                      ]
                    }
                  ]
                }
                """.formatted(
                customerId,
                UUID.randomUUID(),
                location1,
                location2
        );

        mockMvc.perform(
                        post("/api/v1/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(
                                "Order must contain at least one PICKUP and one DELIVERY stop"
                        ));
    }


    // ---------------------------------------------------------
    // 400 - Duplicate stop sequence
    // ---------------------------------------------------------

    @Test
    void createOrder_withDuplicateStopSequence_shouldReturn400()
            throws Exception {

        UUID customerId = UUID.randomUUID();
        UUID pickupLocationId = UUID.randomUUID();
        UUID deliveryLocationId = UUID.randomUUID();

        String requestBody = """
                {
                  "customerId": "%s",
                  "customerReference": "PO-%s",
                  "orderStops": [
                    {
                      "sequenceNumber": 1,
                      "type": "PICKUP",
                      "locationId": "%s"
                    },
                    {
                      "sequenceNumber": 1,
                      "type": "DELIVERY",
                      "locationId": "%s"
                    }
                  ],
                  "cargoItems": [
                    {
                      "description": "Steel coils",
                      "measurements": [
                        {
                          "value": 1000,
                          "unit": "KILOGRAM"
                        }
                      ]
                    }
                  ]
                }
                """.formatted(
                customerId,
                UUID.randomUUID(),
                pickupLocationId,
                deliveryLocationId
        );

        mockMvc.perform(
                        post("/api/v1/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Duplicate stop sequence number: 1"));
    }


    // ---------------------------------------------------------
    // 400 - Invalid time window
    // ---------------------------------------------------------

    @Test
    void createOrder_withInvalidTimeWindow_shouldReturn400()
            throws Exception {

        UUID customerId = UUID.randomUUID();
        UUID pickupLocationId = UUID.randomUUID();
        UUID deliveryLocationId = UUID.randomUUID();

        String requestBody = """
                {
                  "customerId": "%s",
                  "customerReference": "PO-%s",
                  "orderStops": [
                    {
                      "sequenceNumber": 1,
                      "type": "PICKUP",
                      "locationId": "%s",
                      "timeWindowStart": "2026-10-01T12:00:00Z",
                      "timeWindowEnd": "2026-10-01T08:00:00Z"
                    },
                    {
                      "sequenceNumber": 2,
                      "type": "DELIVERY",
                      "locationId": "%s"
                    }
                  ],
                  "cargoItems": [
                    {
                      "description": "Steel coils",
                      "measurements": [
                        {
                          "value": 1000,
                          "unit": "KILOGRAM"
                        }
                      ]
                    }
                  ]
                }
                """.formatted(
                customerId,
                UUID.randomUUID(),
                pickupLocationId,
                deliveryLocationId
        );

        mockMvc.perform(
                        post("/api/v1/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(
                                "Time window start must not be after time window end for stop sequence 1"
                        ));
    }


    // ---------------------------------------------------------
    // 400 - Duplicate CargoUnit
    // ---------------------------------------------------------

    @Test
    void createOrder_withDuplicateMeasurementUnit_shouldReturn400()
            throws Exception {

        UUID customerId = UUID.randomUUID();
        UUID pickupLocationId = UUID.randomUUID();
        UUID deliveryLocationId = UUID.randomUUID();

        String requestBody = """
                {
                  "customerId": "%s",
                  "customerReference": "PO-%s",
                  "orderStops": [
                    {
                      "sequenceNumber": 1,
                      "type": "PICKUP",
                      "locationId": "%s"
                    },
                    {
                      "sequenceNumber": 2,
                      "type": "DELIVERY",
                      "locationId": "%s"
                    }
                  ],
                  "cargoItems": [
                    {
                      "description": "Steel coils",
                      "measurements": [
                        {
                          "value": 1000,
                          "unit": "KILOGRAM"
                        },
                        {
                          "value": 500,
                          "unit": "KILOGRAM"
                        }
                      ]
                    }
                  ]
                }
                """.formatted(
                customerId,
                UUID.randomUUID(),
                pickupLocationId,
                deliveryLocationId
        );

        mockMvc.perform(
                        post("/api/v1/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(
                                "Duplicate cargo measurement unit: KILOGRAM"
                        ));
    }


    // ---------------------------------------------------------
    // 404 - Customer does not exist
    // ---------------------------------------------------------

    @Test
    void createOrder_whenCustomerDoesNotExist_shouldReturn404()
            throws Exception {

        UUID customerId = UUID.randomUUID();
        UUID pickupLocationId = UUID.randomUUID();
        UUID deliveryLocationId = UUID.randomUUID();

        when(masterDataClient.getCustomer(customerId))
                .thenThrow(
                        new MasterDataNotFoundException(
                                "Customer",
                                customerId
                        )
                );

        String requestBody = validRequest(
                customerId,
                pickupLocationId,
                deliveryLocationId,
                "PO-" + UUID.randomUUID()
        );

        mockMvc.perform(
                        post("/api/v1/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Customer not found: " + customerId));
    }


    // ---------------------------------------------------------
    // 404 - Location does not exist
    // ---------------------------------------------------------

    @Test
    void createOrder_whenLocationDoesNotExist_shouldReturn404()
            throws Exception {

        UUID customerId = UUID.randomUUID();
        UUID pickupLocationId = UUID.randomUUID();
        UUID deliveryLocationId = UUID.randomUUID();

        when(masterDataClient.getCustomer(customerId))
                .thenReturn(
                        new MasterDataCustomerResponse(
                                customerId,
                                "NS"
                        )
                );

        when(masterDataClient.getLocation(pickupLocationId))
                .thenThrow(
                        new MasterDataNotFoundException(
                                "Location",
                                pickupLocationId
                        )
                );

        String requestBody = validRequest(
                customerId,
                pickupLocationId,
                deliveryLocationId,
                "PO-" + UUID.randomUUID()
        );

        mockMvc.perform(
                        post("/api/v1/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isNotFound());
    }


    // ---------------------------------------------------------
    // 409 - Duplicate customer reference
    // ---------------------------------------------------------

    @Test
    void createOrder_withDuplicateCustomerReference_shouldReturn409()
            throws Exception {

        UUID customerId = UUID.randomUUID();

        UUID pickup1 = UUID.randomUUID();
        UUID delivery1 = UUID.randomUUID();

        UUID pickup2 = UUID.randomUUID();
        UUID delivery2 = UUID.randomUUID();

        String customerReference =
                "PO-" + UUID.randomUUID();

        mockMasterData(
                customerId,
                randomShortCode(),
                pickup1,
                delivery1
        );

        String firstRequest = validRequest(
                customerId,
                pickup1,
                delivery1,
                customerReference
        );

        mockMvc.perform(
                        post("/api/v1/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(firstRequest)
                )
                .andExpect(status().isCreated());


        mockMasterData(
                customerId,
                randomShortCode(),
                pickup2,
                delivery2
        );

        String secondRequest = validRequest(
                customerId,
                pickup2,
                delivery2,
                customerReference
        );

        mockMvc.perform(
                        post("/api/v1/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(secondRequest)
                )
                .andExpect(status().isConflict());
    }


    // ---------------------------------------------------------
    // 503 - Master Data unavailable
    // ---------------------------------------------------------

    @Test
    void createOrder_whenMasterDataServiceUnavailable_shouldReturn503()
            throws Exception {

        UUID customerId = UUID.randomUUID();
        UUID pickupLocationId = UUID.randomUUID();
        UUID deliveryLocationId = UUID.randomUUID();

        when(masterDataClient.getCustomer(customerId))
                .thenThrow(
                        new MasterDataUnavailableException(
                                new RuntimeException(
                                        "Connection refused"
                                )
                        )
                );

        String requestBody = validRequest(
                customerId,
                pickupLocationId,
                deliveryLocationId,
                "PO-" + UUID.randomUUID()
        );

        mockMvc.perform(
                        post("/api/v1/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message")
                        .value(
                                "Master Data Service unavailable"
                        ));
    }


    // ---------------------------------------------------------
    // 400 - Bean Validation
    // ---------------------------------------------------------

    @Test
    void createOrder_withMissingCustomerId_shouldReturn400()
            throws Exception {

        UUID pickupLocationId = UUID.randomUUID();
        UUID deliveryLocationId = UUID.randomUUID();

        String requestBody = """
                {
                  "customerReference": "PO-4711",
                  "orderStops": [
                    {
                      "sequenceNumber": 1,
                      "type": "PICKUP",
                      "locationId": "%s"
                    },
                    {
                      "sequenceNumber": 2,
                      "type": "DELIVERY",
                      "locationId": "%s"
                    }
                  ],
                  "cargoItems": [
                    {
                      "description": "Steel coils",
                      "measurements": [
                        {
                          "value": 1000,
                          "unit": "KILOGRAM"
                        }
                      ]
                    }
                  ]
                }
                """.formatted(
                pickupLocationId,
                deliveryLocationId
        );

        mockMvc.perform(
                        post("/api/v1/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }


    // =========================================================
    // Helper
    // =========================================================

    private void mockMasterData(
            UUID customerId,
            String shortCode,
            UUID pickupLocationId,
            UUID deliveryLocationId
    ) {

        when(masterDataClient.getCustomer(customerId))
                .thenReturn(
                        new MasterDataCustomerResponse(
                                customerId,
                                shortCode
                        )
                );

        when(masterDataClient.getLocation(pickupLocationId))
                .thenReturn(
                        new MasterDataLocationResponse(
                                pickupLocationId
                        )
                );

        when(masterDataClient.getLocation(deliveryLocationId))
                .thenReturn(
                        new MasterDataLocationResponse(
                                deliveryLocationId
                        )
                );
    }

    private String randomShortCode() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }

    private String validRequest(
            UUID customerId,
            UUID pickupLocationId,
            UUID deliveryLocationId,
            String customerReference
    ) {

        return """
                {
                  "customerId": "%s",
                  "customerReference": "%s",
                  "specialRequirements": "Curtainsider required",
                  "orderStops": [
                    {
                      "sequenceNumber": 1,
                      "type": "PICKUP",
                      "locationId": "%s",
                      "timeWindowStart": "2026-10-01T08:00:00Z",
                      "timeWindowEnd": "2026-10-01T10:00:00Z",
                      "instructions": "Loading at gate 3",
                      "reference": "PICKUP-4711"
                    },
                    {
                      "sequenceNumber": 2,
                      "type": "DELIVERY",
                      "locationId": "%s",
                      "timeWindowStart": "2026-10-01T14:00:00Z",
                      "timeWindowEnd": "2026-10-01T16:00:00Z",
                      "instructions": "Unload at ramp 7",
                      "reference": "DELIVERY-4711"
                    }
                  ],
                  "cargoItems": [
                    {
                      "description": "Steel coils",
                      "measurements": [
                        {
                          "value": 4,
                          "unit": "PIECE"
                        },
                        {
                          "value": 12500,
                          "unit": "KILOGRAM"
                        }
                      ],
                      "notes": "Keep dry during transport"
                    }
                  ]
                }
                """.formatted(
                customerId,
                customerReference,
                pickupLocationId,
                deliveryLocationId
        );
    }
}