package com.example.budgetmanager.controller;

import com.example.budgetmanager.command.ExpenseCommand;
import com.example.budgetmanager.domain.Expense;
import com.example.budgetmanager.dto.ExpenseDTO;
import com.example.budgetmanager.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/expense")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;
    private final ModelMapper modelMapper;

    @PostMapping("/{budgetName}")
    public ResponseEntity<ExpenseDTO> save(@Valid ExpenseCommand expenseCommand, @PathVariable("budgetName") String budgetName) {
        return new ResponseEntity<>(modelMapper
                .map(expenseService
                        .save(modelMapper
                                .map(expenseCommand, Expense.class), budgetName), ExpenseDTO.class), HttpStatus.OK);
    }

}
