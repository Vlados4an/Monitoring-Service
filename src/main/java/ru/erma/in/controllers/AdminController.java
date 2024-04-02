package ru.erma.in.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.erma.dto.*;
import ru.erma.exception.TypeNotFoundException;
import ru.erma.service.AuditService;
import ru.erma.service.ReadingStructureService;
import ru.erma.service.SecurityService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AuditService auditService;
    private final ReadingStructureService readingStructureService;
    private final SecurityService securityService;

    @Operation(summary = "Get all audits")
    @GetMapping("/audits")
    public ResponseEntity<List<AuditDTO>> getAllAudits(){
        return ResponseEntity.ok(auditService.getAllAudits());
    }

    @Operation(summary = "Add a reading type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reading type added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    public ResponseEntity<SuccessResponse> addReadingType(@Valid @RequestBody @Parameter(description = "Admin request")
                                                              AdminRequest adminRequest){
        readingStructureService.addReadingType(adminRequest.type());
        String message = "Reading type added successfully!";
        return ResponseEntity.ok(new SuccessResponse(message));

    }

    @Operation(summary = "Remove a reading type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reading type removed successfully"),
            @ApiResponse(responseCode = "404", description = "Reading type not found")
    })
    @DeleteMapping("/{type}")
    public ResponseEntity<SuccessResponse> removeReadingType(@PathVariable @Parameter(description = "Admin request")
                                                                 String type){
        boolean removed = readingStructureService.removeReadingType(type);
        if(removed) {
            String message = "Reading type removed successfully!";
            return ResponseEntity.ok(new SuccessResponse(message));
        } else throw new TypeNotFoundException("Reading type not found");

    }

    @Operation(summary = "Assign admin role to a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully assigned the admin role"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PutMapping
    public ResponseEntity<SuccessResponse> assignAdmin(@Valid @RequestBody @Parameter(description = "Assign DTO")
                                                           AssignDTO assignDTO) {
        securityService.assignAdmin(assignDTO.username());
        String message = "User with username " + assignDTO.username() + " successfully assigned the admin role.";
        return ResponseEntity.ok(new SuccessResponse(message));
    }
}
