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
import ru.erma.dto.AdminRequest;
import ru.erma.dto.AssignDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class tests the AdminController class.
 * It uses the Mockito framework for mocking objects and JUnit for running the tests.
 * The class is annotated with @ExtendWith(MockitoExtension.class) to integrate Mockito and JUnit.
 */
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
class AdminControllerTest extends AbstractTestContainerConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Tests that the getAllAudits method correctly retrieves all audits.
     * It creates an AuditListDTO, mocks the getAllAudits method of the AuditService to return the AuditListDTO,
     * performs a GET request to "/admin/audits", and asserts that the status is OK and that the response body exists.
     * It also verifies that the getAllAudits method of the AuditService was called once.
     */

    @Test
    @DisplayName("GetAllAudits returns all audits")
    void getAllAudits_returnsAllAudits() throws Exception {
        mockMvc.perform(get("/admin/audits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
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
    }

    @Test
    @DisplayName("AddReadingType returns 400 for invalid request")
    void addReadingType_returns400ForInvalidRequest() throws Exception {
        String invalidAdminJson = "{ \"invalid\": \"json\" }";

        mockMvc.perform(post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidAdminJson))
                .andExpect(status().isBadRequest());
    }





    @Test
    @DisplayName("RemoveReadingType returns 404 for non-existing type")
    void removeReadingType_returns404ForNonExistingType() throws Exception {
        String nonExistingType = "non_existing_type";

        mockMvc.perform(delete("/admin/" + nonExistingType))
                .andExpect(status().isNotFound());
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
        String type = "heating";

        mockMvc.perform(delete("/admin/" + type))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Reading type removed successfully!"));
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
        AssignDTO assignDTO = new AssignDTO("test_user");
        String assignJson = objectMapper.writeValueAsString(assignDTO);

        mockMvc.perform(put("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(assignJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User with username " + assignDTO.username() + " successfully assigned the admin role."));

    }

    @Test
    @DisplayName("AssignAdmin returns 400 for invalid request")
    void assignAdmin_returns400ForInvalidRequest() throws Exception {
        String invalidAssignJson = "{ \"invalid\": \"json\" }";

        mockMvc.perform(put("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidAssignJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("AddReadingType returns 403 for non-admin user")
    void addReadingType_returns403ForNonAdminUser() throws Exception {
        AdminRequest adminRequest = new AdminRequest("solyara");
        String adminJson = objectMapper.writeValueAsString(adminRequest);

        mockMvc.perform(post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adminJson))
                .andExpect(status().isForbidden());
    }
}