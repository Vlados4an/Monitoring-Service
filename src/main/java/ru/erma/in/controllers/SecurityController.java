package ru.erma.in.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(value = "Security Controller", description = "Operations pertaining to user authentication and registration")
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
    @ApiOperation(value = "Authorize user in application")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@ApiParam(value = "Security request", required = true)
                                                 @Valid @RequestBody SecurityDTO dto) {
        JwtResponse response = securityService.authorization(dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Register the player in application
     *
     * @param dto the security request
     * @return response entity
     */
    @ApiOperation(value = "Register the user in application")
    @PostMapping("/registration")
    public ResponseEntity<SuccessResponse> registration(@ApiParam(value = "Security request", required = true)
                                                            @Valid @RequestBody SecurityDTO dto) {
        UserEntity register = securityService.register(dto);
        String message = "User with username " + register.getUsername() + " successfully created.";
        return ResponseEntity.ok(new SuccessResponse(message));
    }
}
