package com.example.budgetmanager.controller;

import com.example.budgetmanager.model.AuthenticationRequest;
import com.example.budgetmanager.model.AuthenticationResponse;
import com.example.budgetmanager.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication controller", description = "Thanks to authentication api, we can got authentication")
@RequestMapping("/api/v1/authentication")
public class AuthController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Get authentication token", description = "The endpoint through which we can got a new auth token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Got the token",
            content = { @Content(mediaType = "application/json",
            schema = @Schema(implementation = AuthenticationResponse.class))})
    })
    @PostMapping
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return new ResponseEntity<>(authenticationService.authenticate(request), HttpStatus.OK);
    }
}