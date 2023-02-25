package com.example.budgetmanager.controller;

import com.example.budgetmanager.command.CustomerCommand;
import com.example.budgetmanager.domain.Customer;
import com.example.budgetmanager.dto.CustomerDTO;
import com.example.budgetmanager.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<CustomerDTO> save(CustomerCommand customerCommand) {
        return new ResponseEntity<>(modelMapper
                .map(customerService
                        .save(modelMapper
                                .map(customerCommand, Customer.class)), CustomerDTO.class), HttpStatus.CREATED);
    }

}
