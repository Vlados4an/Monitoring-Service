package ru.erma.in.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.erma.dto.AdminRequest;
import ru.erma.dto.AssignDTO;
import ru.erma.dto.AuditListDTO;
import ru.erma.dto.SuccessResponse;
import ru.erma.exception.TypeNotFoundException;
import ru.erma.service.AuditService;
import ru.erma.service.ReadingStructureService;
import ru.erma.service.SecurityService;

@Api(value = "Admin Controller", description = "Operations pertaining to admin functionalities")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AuditService auditService;
    private final ReadingStructureService readingStructureService;
    private final SecurityService securityService;

    @ApiOperation(value = "Get all audits for a user")
    @GetMapping("/audits")
    public ResponseEntity<AuditListDTO> getAllAudits(){

        return
                ResponseEntity.ok(auditService.getAllAudits());
    }

    @ApiOperation(value = "Add a reading type")
    @PostMapping
    public ResponseEntity<SuccessResponse> addReadingType(@ApiParam(value = "Added type", required = true)
                                                              @Valid @RequestBody AdminRequest adminRequest){

        readingStructureService.addReadingType(adminRequest.type());
        String message = "Reading type added successfully!";
        return ResponseEntity.ok(new SuccessResponse(message));

    }

    @ApiOperation(value = "Remove a reading type")
    @DeleteMapping("/{type}")
    public ResponseEntity<SuccessResponse> removeReadingType(@ApiParam(value = "Removed type", required = true)
                                                                 @PathVariable String type){

        boolean removed = readingStructureService.removeReadingType(type);
        if(removed) {
            String message = "Reading type removed successfully!";
            return ResponseEntity.ok(new SuccessResponse(message));
        } else throw new TypeNotFoundException("Reading type not found");

    }

    @ApiOperation(value = "Assign a user to the admin role")
    @PutMapping
    public ResponseEntity<SuccessResponse> assignAdmin(@ApiParam(value = "Username of the user to be assigned the admin role", required = true)
                                                           @RequestBody AssignDTO assignDTO) {
        securityService.assignAdmin(assignDTO.username());
        String message = "User with username " + assignDTO.username() + " successfully assigned the admin role.";
        return ResponseEntity.ok(new SuccessResponse(message));
    }
}
