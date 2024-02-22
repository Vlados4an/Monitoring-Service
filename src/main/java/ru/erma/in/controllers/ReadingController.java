package ru.erma.in.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/readings")
@Validated
public class ReadingController {
    private final ReadingService readingService;

    @Operation(summary = "Get actual readings")
    @GetMapping("/actual/{username}")
    public ResponseEntity<ReadingDTO> getActualReadings(@PathVariable @Parameter(description = "Username")
                                                            String username){
        validateUsername(username);

        ReadingDTO readingDTO = readingService.getActualReadings(username);
        return ResponseEntity.ok(readingDTO);
    }

    @Operation(summary = "Get readings history")
    @GetMapping("/history/{username}")
    public ResponseEntity<ReadingListDTO> getReadingsHistory(@PathVariable @Parameter(description = "Username")
                                                                 String username){
        validateUsername(username);

        ReadingListDTO readingListDTO = readingService.getReadingHistory(username);
        return ResponseEntity.ok(readingListDTO);
    }

    @Operation(summary = "Get readings for a specific month")
    @GetMapping("/{username}/{month}/{year}")
    public ResponseEntity<ReadingListDTO> getReadingsForMonth( @PathVariable @Parameter(description = "Username") String username,
                                                               @PathVariable @Parameter(description = "Month") Integer month,
                                                               @PathVariable @Parameter(description = "Year") Integer year) {
        validateUsername(username);

        ReadingListDTO readingListDTO = readingService.getReadingsForMonth(username, month, year);
        return ResponseEntity.ok(readingListDTO);
    }

    @Operation(summary = "Submit readings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reading submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    public ResponseEntity<SuccessResponse> submitReadings(@Valid @RequestBody  @Parameter(description = "Reading request")
                                                              ReadingRequest request){
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
