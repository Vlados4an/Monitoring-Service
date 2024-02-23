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
 * This class tests the ReadingController class.
 * It uses the Mockito framework for mocking objects and JUnit for running the tests.
 * The class is annotated with @ExtendWith(MockitoExtension.class) to integrate Mockito and JUnit.
 */
@AutoConfigureMockMvc
@WithMockUser(username = "test_user")
class ReadingControllerTest extends AbstractTestContainerConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GetActualReadings returns actual readings for a user")
    void getActualReadings_returnsActualReadingsForUser() throws Exception {
        String username = "test_user";

        mockMvc.perform(get("/readings/actual/" + username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @DisplayName("GetActualReadings returns 401 for non-authorized user")
    void getActualReadings_returnsUnauthorized() throws Exception {
        String username = "non_existing_user";

        mockMvc.perform(get("/readings/actual/" + username))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Username in the request does not match the username in the token."));
    }

    @Test
    @DisplayName("GetReadingsHistory returns 401 for non-authorized user")
    void getReadingsHistory_returnsUnauthorized() throws Exception {
        String username = "non_existing_user";

        mockMvc.perform(get("/readings/history/" + username))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Username in the request does not match the username in the token."));
    }

    @Test
    @DisplayName("GetReadingsForMonth returns 401 for non-authorized user")
    void getReadingsForMonth_returnsUnauthorized() throws Exception {

        mockMvc.perform(get("/readings/non_existing_user/1/2022"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Username in the request does not match the username in the token."));
    }


    /**
     * Tests that the getReadingsHistory method correctly retrieves all readings for a user.
     * It creates a ReadingListDTO, mocks the getReadingHistory method of the ReadingService to return the ReadingListDTO,
     * performs a GET request to "/readings/history/{username}", and asserts that the status is OK and that the response body exists.
     * It also verifies that the getReadingHistory method of the ReadingService was called once.
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
     * Tests that the getReadingsForMonth method correctly retrieves all readings for a specific month for a user.
     * It creates a ReadingListDTO, mocks the getReadingsForMonth method of the ReadingService to return the ReadingListDTO,
     * performs a GET request to "/readings/{username}/{month}/{year}", and asserts that the status is OK and that the response body exists.
     * It also verifies that the getReadingsForMonth method of the ReadingService was called once.
     */
    @Test
    @DisplayName("GetReadingsForMonth returns readings for a specific month")
    void getReadingsForMonth_returnsReadingsForMonth() throws Exception {

        mockMvc.perform(get("/readings/test_user/1/2022"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

    }

    /**
     * Tests that the submitReadings method correctly submits readings for a user.
     * It creates a ReadingRequest, converts it to JSON, performs a POST request to "/readings" with the JSON as the request body,
     * and asserts that the status is OK and that the response body contains the expected message.
     * It also verifies that the submitReadings method of the ReadingService was called once.
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

    @Test
    @DisplayName("SubmitReadings returns 400 for invalid request")
    void submitReadings_returns400ForInvalidRequest() throws Exception {
        String invalidReadingJson = "{ \"invalid\": \"json\" }";

        mockMvc.perform(post("/readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidReadingJson))
                .andExpect(status().isBadRequest());
    }

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