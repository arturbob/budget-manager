package com.example.budgetmanager.controller;

import com.example.budgetmanager.command.ExpenseCommand;
import com.example.budgetmanager.dto.ExpenseDTO;
import com.example.budgetmanager.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/expense")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<ExpenseDTO> save(@RequestBody @Valid ExpenseCommand expenseCommand) {
        return new ResponseEntity<>(modelMapper
                .map(expenseService
                        .save(expenseCommand), ExpenseDTO.class), HttpStatus.CREATED);
    }

    @GetMapping("/{budgetName}")
    public ResponseEntity<Page<ExpenseDTO>> findAll(@PageableDefault Pageable pageable, @PathVariable("budgetName") String budgetName) {
        return new ResponseEntity<>(expenseService.findAllWithPagination(pageable, budgetName)
                .map(expense -> modelMapper
                        .map(expense, ExpenseDTO.class)), HttpStatus.OK);
    }
}
