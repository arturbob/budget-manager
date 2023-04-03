package com.example.budgetmanager.controller;

import com.example.budgetmanager.command.CustomerCommand;
import com.example.budgetmanager.domain.Customer;
import com.example.budgetmanager.dto.CustomerDTO;
import com.example.budgetmanager.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@Tag(name = "Api for customer process", description = "Thanks to customer api, we can create a new customer")
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    @Operation(summary = "Save a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class))})
    })
    @PostMapping
    public ResponseEntity<CustomerDTO> save(@RequestBody @Valid CustomerCommand customerCommand) {
        return new ResponseEntity<>(modelMapper
                .map(customerService
                        .save(modelMapper
                                .map(customerCommand, Customer.class)), CustomerDTO.class), HttpStatus.CREATED);
    }
}
