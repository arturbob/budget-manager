package com.example.budgetmanager.controller;

import com.example.budgetmanager.model.JWTRequest;
import com.example.budgetmanager.model.JWTResponse;
import com.example.budgetmanager.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authentication")
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<JWTResponse> authenticate(@RequestBody JWTRequest request) {
        return new ResponseEntity<>(authenticationService.authenticate(request), HttpStatus.OK);
    }
}