package com.example.budgetmanager.service;

import com.example.budgetmanager.config.security.jwt.JwtService;
import com.example.budgetmanager.domain.Customer;
import com.example.budgetmanager.exception.CustomerNotFoundException;
import com.example.budgetmanager.model.JWTRequest;
import com.example.budgetmanager.model.JWTResponse;
import com.example.budgetmanager.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;

    public JWTResponse authenticate(JWTRequest request) {
        Customer customer = customerRepository.findByLogin(request.getLogin())
                .orElseThrow(CustomerNotFoundException::new);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
        String jwtToken = jwtService.generateToken(customer);
        return JWTResponse.builder()
                .token(jwtToken)
                .build();
    }
}
