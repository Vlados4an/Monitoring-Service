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

@ExtendWith(MockitoExtension.class)
class SecurityControllerTest {

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private SecurityController securityController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    /**
     * Initializes the MockMvc and ObjectMapper instances before each test.
     */
    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(securityController).build();
        objectMapper = new ObjectMapper();
    }

    /**
     * Tests that the login method correctly returns a JwtResponse when the credentials are valid.
     * It creates a SecurityDTO and a JwtResponse, mocks the authorization method of the SecurityService to return the JwtResponse,
     * performs a POST request to "/auth/login" with the SecurityDTO as the request body, and asserts that the status is OK and that the response body matches the JwtResponse.
     * It also verifies that the authorization method of the SecurityService was called once with the SecurityDTO.
     */
    @Test
    @DisplayName("Login returns JwtResponse when credentials are valid")
    void login_returnsJwtResponseWhenCredentialsAreValid() throws Exception {
        SecurityDTO securityDTO = new SecurityDTO("testUser", "testPass");
        JwtResponse jwtResponse = new JwtResponse("testUser", "accessToken","refreshToken");
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
     * Tests that the registration method correctly returns a success response when the username is not taken.
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