package ru.erma.in.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.erma.config.AbstractTestContainerConfig;
import ru.erma.dto.ReadingRequest;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class is responsible for testing the ReadingController.
 * It extends AbstractTestContainerConfig to use a PostgreSQL test container.
 * It is annotated with @AutoConfigureMockMvc to set up a MockMvc instance.
 * It is also annotated with @WithMockUser(username = "test_user") to set up a mock user for the tests.
 */
@AutoConfigureMockMvc
@WithMockUser(username = "test_user")
class ReadingControllerTest extends AbstractTestContainerConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * This test checks if the getActualReadings method of the ReadingController returns actual readings for a user.
     * It performs a GET request to "/readings/actual/{username}" and expects the status to be OK and the response body to exist.
     */
    @Test
    @DisplayName("GetActualReadings returns actual readings for a user")
    void getActualReadings_returnsActualReadingsForUser() throws Exception {
        String username = "test_user";

        mockMvc.perform(get("/readings/actual/" + username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    /**
     * This test checks if the getActualReadings method of the ReadingController returns a 401 status for a non-authorized user.
     * It performs a GET request to "/readings/actual/{username}" and expects the status to be 401 (Unauthorized) and the response body to contain the message "Username in the request does not match the username in the token.".
     */
    @Test
    @DisplayName("GetActualReadings returns 401 for non-authorized user")
    void getActualReadings_returnsUnauthorized() throws Exception {
        String username = "non_existing_user";

        mockMvc.perform(get("/readings/actual/" + username))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Username in the request does not match the username in the token."));
    }

    /**
     * This test checks if the getReadingsHistory method of the ReadingController returns a 401 status for a non-authorized user.
     * It performs a GET request to "/readings/history/{username}" and expects the status to be 401 (Unauthorized) and the response body to contain the message "Username in the request does not match the username in the token.".
     */
    @Test
    @DisplayName("GetReadingsHistory returns 401 for non-authorized user")
    void getReadingsHistory_returnsUnauthorized() throws Exception {
        String username = "non_existing_user";

        mockMvc.perform(get("/readings/history/" + username))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Username in the request does not match the username in the token."));
    }

    /**
     * This test checks if the getReadingsForMonth method of the ReadingController returns a 401 status for a non-authorized user.
     * It performs a GET request to "/readings/{username}/{month}/{year}" and expects the status to be 401 (Unauthorized) and the response body to contain the message "Username in the request does not match the username in the token.".
     */
    @Test
    @DisplayName("GetReadingsForMonth returns 401 for non-authorized user")
    void getReadingsForMonth_returnsUnauthorized() throws Exception {

        mockMvc.perform(get("/readings/non_existing_user/1/2022"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Username in the request does not match the username in the token."));
    }


    /**
     * This test checks if the getReadingsHistory method of the ReadingController returns history readings for a user.
     * It performs a GET request to "/readings/history/{username}" and expects the status to be OK and the response body to exist.
     */
    @Test
    @DisplayName("GetReadingsHistory returns history readings for a user")
    void getReadingsHistory_returnsHistoryReadingsForUser() throws Exception {
        String username = "test_user";

        mockMvc.perform(get("/readings/history/" + username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    /**
     * This test checks if the getReadingsForMonth method of the ReadingController returns readings for a specific month for a user.
     * It performs a GET request to "/readings/{username}/{month}/{year}" and expects the status to be OK and the response body to exist.
     */
    @Test
    @DisplayName("GetReadingsForMonth returns readings for a specific month")
    void getReadingsForMonth_returnsReadingsForMonth() throws Exception {

        mockMvc.perform(get("/readings/test_user/1/2022"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    /**
     * This test checks if the submitReadings method of the ReadingController submits readings for a user.
     * It creates a ReadingRequest, converts it to JSON, performs a POST request to "/readings" with the JSON as the request body,
     * and expects the status to be OK and the response body to contain the message "Reading submitted successfully!".
     */
    @Test
    @DisplayName("SubmitReadings submits readings for a user")
    void submitReadings_submitsReadingsForUser() throws Exception {
        Map<String, Integer> values = new HashMap<>();
        ReadingRequest readingRequest = new ReadingRequest("test_user", 2, 2022, values);
        String readingJson = objectMapper.writeValueAsString(readingRequest);

        mockMvc.perform(post("/readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readingJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Reading submitted successfully!"));
    }

    /**
     * This test checks if the submitReadings method of the ReadingController returns a 400 status for an invalid request.
     * It creates an invalid JSON string, performs a POST request to "/readings" with the JSON as the request body,
     * and expects the status to be 400 (Bad Request).
     */
    @Test
    @DisplayName("SubmitReadings returns 400 for invalid request")
    void submitReadings_returns400ForInvalidRequest() throws Exception {
        String invalidReadingJson = "{ \"invalid\": \"json\" }";

        mockMvc.perform(post("/readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidReadingJson))
                .andExpect(status().isBadRequest());
    }

    /**
     * This test checks if the submitReadings method of the ReadingController returns a 403 status for a user with an unknown role.
     * It creates a ReadingRequest, converts it to JSON, performs a POST request to "/readings" with the JSON as the request body,
     * and expects the status to be 403 (Forbidden).
     */
    @Test
    @WithMockUser(roles = "UnknownRole")
    @DisplayName("SubmitReadings returns forbidden")
    void submitReadings_returns403ForNotUser() throws Exception {
        Map<String, Integer> values = new HashMap<>();
        ReadingRequest readingRequest = new ReadingRequest("test_user", 2, 2022, values);
        String readingJson = objectMapper.writeValueAsString(readingRequest);

        mockMvc.perform(post("/readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readingJson))
                .andExpect(status().isForbidden());
    }
}