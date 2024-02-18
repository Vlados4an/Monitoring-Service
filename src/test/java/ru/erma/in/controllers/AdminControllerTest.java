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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.erma.dto.AdminRequest;
import ru.erma.dto.AssignDTO;
import ru.erma.dto.AuditListDTO;
import ru.erma.service.AuditService;
import ru.erma.service.ReadingStructureService;
import ru.erma.service.SecurityService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class tests the AdminController class.
 * It uses the Mockito framework for mocking objects and JUnit for running the tests.
 * The class is annotated with @ExtendWith(MockitoExtension.class) to integrate Mockito and JUnit.
 */
@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private AuditService auditService;

    @Mock
    private SecurityService securityService;

    @Mock
    private ReadingStructureService readingStructureService;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    /**
     * Initializes the MockMvc and ObjectMapper instances before each test.
     */
    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
        objectMapper = new ObjectMapper();
    }

    /**
     * Tests that the getAllAudits method correctly retrieves all audits.
     * It creates an AuditListDTO, mocks the getAllAudits method of the AuditService to return the AuditListDTO,
     * performs a GET request to "/admin/audits", and asserts that the status is OK and that the response body exists.
     * It also verifies that the getAllAudits method of the AuditService was called once.
     */
    @Test
    @DisplayName("GetAllAudits returns all audits")
    void getAllAudits_returnsAllAudits() throws Exception {
        AuditListDTO auditListDTO = new AuditListDTO();
        when(auditService.getAllAudits()).thenReturn(auditListDTO);

        mockMvc.perform(get("/admin/audits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(auditService, times(1)).getAllAudits();
    }

    /**
     * Tests that the addReadingType method correctly adds a reading type.
     * It creates an AdminRequest, converts it to JSON, performs a POST request to "/admin" with the JSON as the request body,
     * and asserts that the status is OK and that the response body contains the expected message.
     * It also verifies that the addReadingType method of the ReadingStructureService was called once with the correct argument.
     */
    @Test
    @DisplayName("AddReadingType adds a reading type successfully")
    void addReadingType_addsReadingTypeSuccessfully() throws Exception {
        AdminRequest adminRequest = new AdminRequest("solyara");
        String adminJson = objectMapper.writeValueAsString(adminRequest);

        mockMvc.perform(post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adminJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Reading type added successfully!"));

        verify(readingStructureService, times(1)).addReadingType(adminRequest.type());
    }

    /**
     * Tests that the removeReadingType method correctly removes a reading type.
     * It creates a reading type, mocks the removeReadingType method of the ReadingStructureService to return true,
     * performs a DELETE request to "/admin/{type}", and asserts that the status is OK and that the response body contains the expected message.
     * It also verifies that the removeReadingType method of the ReadingStructureService was called once with the correct argument.
     */
    @Test
    @DisplayName("RemoveReadingType removes a reading type successfully")
    void removeReadingType_removesReadingTypeSuccessfully() throws Exception {
        String type = "solyara";
        when(readingStructureService.removeReadingType(type)).thenReturn(true);

        mockMvc.perform(delete("/admin/" + type))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Reading type removed successfully!"));

        verify(readingStructureService, times(1)).removeReadingType(type);
    }

    /**
     * Tests that the assignAdmin method correctly assigns a user as admin.
     * It creates an AssignDTO, converts it to JSON, performs a PUT request to "/admin" with the JSON as the request body,
     * and asserts that the status is OK and that the response body contains the expected message.
     * It also verifies that the assignAdmin method of the SecurityService was called once with the correct argument.
     */
    @Test
    @DisplayName("AssignAdmin assigns user as admin successfully")
    void assignAdmin_assignsUserAsAdminSuccessfully() throws Exception {
        AssignDTO assignDTO = new AssignDTO("testUser");
        String assignJson = objectMapper.writeValueAsString(assignDTO);

        mockMvc.perform(put("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(assignJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User with username " + assignDTO.username() + " successfully assigned the admin role."));

        verify(securityService, times(1)).assignAdmin(assignDTO.username());
    }

}