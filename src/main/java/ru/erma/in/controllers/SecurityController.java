package ru.erma.in.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.erma.dto.JwtResponse;
import ru.erma.dto.SecurityDTO;
import ru.erma.dto.SuccessResponse;
import ru.erma.model.UserEntity;
import ru.erma.service.SecurityService;

/**
 * The security controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class SecurityController {

    private final SecurityService securityService;

    /**
     * Authorization player in application
     *
     * @param dto the security request
     * @return response entity
     */
    @Operation(summary = "Authorize user in application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authorized successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody @Parameter(description = "Security request")
                                                 SecurityDTO dto) {
        JwtResponse response = securityService.authorization(dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Register the player in application
     *
     * @param dto the security request
     * @return response entity
     */
    @Operation(summary = "Register the user in application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping("/registration")
    public ResponseEntity<SuccessResponse> registration(@Valid @RequestBody @Parameter(description = "Security request")
                                                            SecurityDTO dto) {
        UserEntity register = securityService.register(dto);
        String message = "User with username " + register.getUsername() + " successfully created.";
        return ResponseEntity.ok(new SuccessResponse(message));
    }
}
