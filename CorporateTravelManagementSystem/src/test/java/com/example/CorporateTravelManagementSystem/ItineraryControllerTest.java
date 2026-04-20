package com.example.CorporateTravelManagementSystem;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ItineraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createReturnsNotFoundWhenTravelRequestDoesNotExist() throws Exception {
        String requestBody = """
                {
                  "travelRequestId": 1,
                  "segmentType": "FLIGHT",
                  "providerName": "Indigo",
                  "bookingReference": "IND123",
                  "departureDateTime": "2026-04-25T10:00:00",
                  "arrivalDateTime": "2026-04-25T12:00:00",
                  "fromLocation": "Hyderabad",
                  "toLocation": "Delhi",
                  "cost": 5000
                }
                """;

        mockMvc.perform(post("/api/itineraries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Travel request not found for id: 1"))
                .andExpect(jsonPath("$.path").value("/api/itineraries"));
    }

    @Test
    void createReturnsBadRequestForInvalidDates() throws Exception {
        String requestBody = """
                {
                  "travelRequestId": 1,
                  "segmentType": "FLIGHT",
                  "providerName": "Indigo",
                  "bookingReference": "IND123",
                  "departureDateTime": "2026-04-25T12:00:00",
                  "arrivalDateTime": "2026-04-25T10:00:00",
                  "fromLocation": "Hyderabad",
                  "toLocation": "Delhi",
                  "cost": 5000
                }
                """;

        mockMvc.perform(post("/api/itineraries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("arrivalDateTime must be after departureDateTime"))
                .andExpect(jsonPath("$.path").value("/api/itineraries"));
    }
}
