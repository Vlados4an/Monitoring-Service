package ru.erma.in.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.erma.dto.ReadingDTO;
import ru.erma.dto.ReadingListDTO;
import ru.erma.dto.ReadingRequest;
import ru.erma.dto.SuccessResponse;
import ru.erma.exception.AuthorizeException;
import ru.erma.service.ReadingService;

@Api(value = "Reading Controller", description = "Operations pertaining to reading functionalities")
@RestController
@RequiredArgsConstructor
@RequestMapping("/readings")
@Validated
public class ReadingController {
    private final ReadingService readingService;

    @ApiOperation(value = "Get actual readings for a user")
    @GetMapping("/actual/{username}")
    public ResponseEntity<ReadingDTO> getActualReadings(@ApiParam(value = "Username of the user", required = true)
                                                            @PathVariable String username){
        validateUsername(username);

        ReadingDTO readingDTO = readingService.getActualReadings(username);
        return ResponseEntity.ok(readingDTO);
    }

    @ApiOperation(value = "Get history readings for a user")
    @GetMapping("/history/{username}")
    public ResponseEntity<ReadingListDTO> getReadingsHistory(@ApiParam(value = "Username of the user", required = true)
                                                                 @PathVariable String username){
        validateUsername(username);

        ReadingListDTO readingListDTO = readingService.getReadingHistory(username);
        return ResponseEntity.ok(readingListDTO);
    }

    @GetMapping("/{username}/{month}/{year}")
    public ResponseEntity<ReadingListDTO> getReadingsForMonth(@ApiParam(value = "Username of the user", required = true) @PathVariable String username,
                                                              @ApiParam(value = "Month for the readings", required = true) @PathVariable Integer month,
                                                              @ApiParam(value = "Year for the readings", required = true) @PathVariable Integer year) {
        validateUsername(username);

        ReadingListDTO readingListDTO = readingService.getReadingsForMonth(username, month, year);
        return ResponseEntity.ok(readingListDTO);
    }

    @ApiOperation(value = "Post submit readings for a user")
    @PostMapping
    public ResponseEntity<SuccessResponse> submitReadings(@ApiParam(value = "Reading request", required = true)
                                                              @Valid @RequestBody ReadingRequest request){
        validateUsername(request.username());

        readingService.submitReadings(request);
        String message = "Reading submitted successfully!";
        return ResponseEntity.ok(new SuccessResponse(message));
    }

    private void validateUsername(String  username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usernameFromToken = authentication.getName();

        if (!usernameFromToken.equals(username)) {
            throw new AuthorizeException("Username in the request does not match the username in the token.");
        }
    }
}
