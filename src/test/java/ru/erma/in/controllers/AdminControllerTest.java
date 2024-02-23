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
 * This class is responsible for testing the AdminController.
 * It extends AbstractTestContainerConfig to use a PostgreSQL test container.
 * It is annotated with @AutoConfigureMockMvc to set up a MockMvc instance.
 * It is also annotated with @WithMockUser(roles = "ADMIN") to set up a mock user for the tests.
 */
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
class AdminControllerTest extends AbstractTestContainerConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * This test checks if the getAllAudits method of the AdminController returns all audits.
     * It performs a GET request to "/admin/audits" and expects the status to be OK and the response body to exist.
     */
    @Test
    @DisplayName("GetAllAudits returns all audits")
    void getAllAudits_returnsAllAudits() throws Exception {
        mockMvc.perform(get("/admin/audits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    /**
     * This test checks if the addReadingType method of the AdminController adds a reading type successfully.
     * It creates an AdminRequest, converts it to JSON, performs a POST request to "/admin" with the JSON as the request body,
     * and expects the status to be OK and the response body to contain the message "Reading type added successfully!".
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

    /**
     * This test checks if the addReadingType method of the AdminController returns a 400 status for an invalid request.
     * It creates an invalid JSON string, performs a POST request to "/admin" with the JSON as the request body,
     * and expects the status to be 400 (Bad Request).
     */
    @Test
    @DisplayName("AddReadingType returns 400 for invalid request")
    void addReadingType_returns400ForInvalidRequest() throws Exception {
        String invalidAdminJson = "{ \"invalid\": \"json\" }";

        mockMvc.perform(post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidAdminJson))
                .andExpect(status().isBadRequest());
    }

    /**
     * This test checks if the removeReadingType method of the AdminController returns a 404 status for a non-existing type.
     * It creates a string for a non-existing type, performs a DELETE request to "/admin/{type}",
     * and expects the status to be 404 (Not Found).
     */
    @Test
    @DisplayName("RemoveReadingType returns 404 for non-existing type")
    void removeReadingType_returns404ForNonExistingType() throws Exception {
        String nonExistingType = "non_existing_type";

        mockMvc.perform(delete("/admin/" + nonExistingType))
                .andExpect(status().isNotFound());
    }

    /**
     * This test checks if the removeReadingType method of the AdminController removes a reading type successfully.
     * It creates a string for a type, performs a DELETE request to "/admin/{type}",
     * and expects the status to be OK and the response body to contain the message "Reading type removed successfully!".
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
     * This test checks if the assignAdmin method of the AdminController assigns a user as admin successfully.
     * It creates an AssignDTO, converts it to JSON, performs a PUT request to "/admin" with the JSON as the request body,
     * and expects the status to be OK and the response body to contain the message "User with username {username} successfully assigned the admin role.".
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

    /**
     * This test checks if the assignAdmin method of the AdminController returns a 400 status for an invalid request.
     * It creates an invalid JSON string, performs a PUT request to "/admin" with the JSON as the request body,
     * and expects the status to be 400 (Bad Request).
     */
    @Test
    @DisplayName("AssignAdmin returns 400 for invalid request")
    void assignAdmin_returns400ForInvalidRequest() throws Exception {
        String invalidAssignJson = "{ \"invalid\": \"json\" }";

        mockMvc.perform(put("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidAssignJson))
                .andExpect(status().isBadRequest());
    }

    /**
     * This test checks if the addReadingType method of the AdminController returns a 403 status for a non-admin user.
     * It creates an AdminRequest, converts it to JSON, performs a POST request to "/admin" with the JSON as the request body,
     * and expects the status to be 403 (Forbidden).
     */
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