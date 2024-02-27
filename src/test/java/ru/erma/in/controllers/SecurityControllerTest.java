package ru.erma.in.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.erma.config.AbstractTestContainerConfig;
import ru.erma.dto.JwtResponse;
import ru.erma.dto.SecurityDTO;
import ru.erma.model.UserEntity;
import ru.erma.service.SecurityService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class is responsible for testing the SecurityController.
 * It extends AbstractTestContainerConfig to use a PostgreSQL test container.
 * It is annotated with @AutoConfigureMockMvc to set up a MockMvc instance.
 * It is also annotated with @WithMockUser(username = "testUser") to set up a mock user for the tests.
 */
@AutoConfigureMockMvc
@WithMockUser(username = "testUser")
class SecurityControllerTest extends AbstractTestContainerConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SecurityService securityService;

    /**
     * This test checks if the login method of the SecurityController returns a JwtResponse when the credentials are valid.
     * It creates a SecurityDTO and a JwtResponse, mocks the authorization method of the SecurityService to return the JwtResponse,
     * performs a POST request to "/auth/login" with the SecurityDTO as the request body, and asserts that the status is OK and that the response body matches the JwtResponse.
     * It also verifies that the authorization method of the SecurityService was called once with the SecurityDTO.
     */
    @Test
    @DisplayName("Login returns JwtResponse when credentials are valid")
    void login_returnsJwtResponseWhenCredentialsAreValid() throws Exception {
        SecurityDTO securityDTO = new SecurityDTO("testUser", "testPass");
        JwtResponse jwtResponse = new JwtResponse("testUser", "accessToken", "refreshToken");
        when(securityService.authorization(securityDTO)).thenReturn(jwtResponse);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(securityDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"));

        verify(securityService, times(1)).authorization(securityDTO);
    }

    /**
     * This test checks if the registration method of the SecurityController returns a success response when the username is not taken.
     * It creates a SecurityDTO and a UserEntity, mocks the register method of the SecurityService to return the UserEntity,
     * performs a POST request to "/auth/registration" with the SecurityDTO as the request body, and asserts that the status is OK and that the response body contains the expected message.
     * It also verifies that the register method of the SecurityService was called once with the SecurityDTO.
     */
    @Test
    @DisplayName("Registration returns success response when username is not taken")
    void registration_returnsSuccessResponseWhenUsernameIsNotTaken() throws Exception {
        SecurityDTO securityDTO = new SecurityDTO("testUser", "testPass");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        when(securityService.register(securityDTO)).thenReturn(userEntity);

        mockMvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(securityDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User with username testUser successfully created."));

        verify(securityService, times(1)).register(securityDTO);
    }

}