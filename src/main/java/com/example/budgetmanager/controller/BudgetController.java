package com.example.budgetmanager.controller;

import com.example.budgetmanager.command.BudgetCommand;
import com.example.budgetmanager.domain.Budget;
import com.example.budgetmanager.dto.BudgetDTO;
import com.example.budgetmanager.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/budgets")
public class BudgetController {
    private final BudgetService budgetService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<BudgetDTO> save(@RequestBody @Valid BudgetCommand budgetCommand) {
        return new ResponseEntity<>(modelMapper
                .map(budgetService
                        .save(modelMapper
                                .map(budgetCommand, Budget.class)), BudgetDTO.class), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<BudgetDTO>> listAll(@PageableDefault Pageable pageable) {
        return new ResponseEntity<>(budgetService.listAllWithPagination(pageable)
                .map(budget -> modelMapper
                        .map(budget, BudgetDTO.class)), HttpStatus.OK);
    }
}
