package ru.erma.in.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import ru.erma.dto.ReadingDTO;
import ru.erma.dto.ReadingListDTO;
import ru.erma.dto.ReadingRequest;
import ru.erma.service.ReadingService;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class tests the ReadingController class.
 * It uses the Mockito framework for mocking objects and JUnit for running the tests.
 * The class is annotated with @ExtendWith(MockitoExtension.class) to integrate Mockito and JUnit.
 */
@ExtendWith(MockitoExtension.class)
class ReadingControllerTest {

    @Mock
    private ReadingService readingService;

    @Mock
    private Validator validator;

    @InjectMocks
    private ReadingController readingController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    /**
     * Initializes the MockMvc and ObjectMapper instances before each test.
     */
    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(readingController)
                .setValidator(validator)
                .build();

        objectMapper = new ObjectMapper();

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", "password");
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    /**
     * Tests that the getActualReadings method correctly retrieves all readings for a user.
     * It creates a ReadingDTO, mocks the getActualReadings method of the ReadingService to return the ReadingDTO,
     * performs a GET request to "/readings/actual/{username}", and asserts that the status is OK and that the response body exists.
     * It also verifies that the getActualReadings method of the ReadingService was called once.
     */
    @Test
    @DisplayName("GetActualReadings returns actual readings for a user")
    void getActualReadings_returnsActualReadingsForUser() throws Exception {
        String username = "testUser";
        ReadingDTO readingDTO = new ReadingDTO();
        when(readingService.getActualReadings(username)).thenReturn(readingDTO);

        mockMvc.perform(get("/readings/actual/" + username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(readingService, times(1)).getActualReadings(username);
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
        String username = "testUser";
        ReadingListDTO readingListDTO = new ReadingListDTO();
        when(readingService.getReadingHistory(username)).thenReturn(readingListDTO);

        mockMvc.perform(get("/readings/history/" + username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(readingService, times(1)).getReadingHistory(username);
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
        String username = "testUser";
        ReadingListDTO readingListDTO = new ReadingListDTO();
        when(readingService.getReadingsForMonth(username, 1, 2022)).thenReturn(readingListDTO);

        mockMvc.perform(get("/readings/testUser/1/2022"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(readingService, times(1)).getReadingsForMonth(username, 1, 2022);
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
        values.put("heating", 20);
        values.put("cold water", 30);
        values.put("hot water", 40);
        ReadingRequest readingRequest = new ReadingRequest("testUser", 1, 2022, values);
        String readingJson = objectMapper.writeValueAsString(readingRequest);

        mockMvc.perform(post("/readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readingJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Reading submitted successfully!"));

        verify(readingService, times(1)).submitReadings(readingRequest);
    }

}