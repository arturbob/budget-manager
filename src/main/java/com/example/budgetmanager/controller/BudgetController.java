package com.example.budgetmanager.controller;

import com.example.budgetmanager.command.BudgetCommand;
import com.example.budgetmanager.domain.Budget;
import com.example.budgetmanager.dto.BudgetDTO;
import com.example.budgetmanager.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BudgetController {
    private final BudgetService budgetService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<BudgetDTO> save(BudgetCommand budgetCommand) {
        return new ResponseEntity<>(modelMapper
                .map(budgetService
                        .save(modelMapper
                                .map(budgetCommand, Budget.class)), BudgetDTO.class), HttpStatus.CREATED);
    }
}
