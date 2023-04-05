package com.example.budgetmanager.controller;

import com.example.budgetmanager.command.ExpenseCommand;
import com.example.budgetmanager.dto.ExpenseDTO;
import com.example.budgetmanager.model.BudgetStatus;
import com.example.budgetmanager.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Expense controller", description = "Thanks to expense budget, we can add new expense into our budget and list our expenses")
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;
    private final ModelMapper modelMapper;

    @Operation(summary = "Save an expense", description = "The endpoint through which we can create a new expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Expense added",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BudgetStatus.class))})
    })
    @PostMapping
    public ResponseEntity<BudgetStatus> save(@RequestBody @Valid ExpenseCommand expenseCommand) {
        return new ResponseEntity<>(expenseService.save(expenseCommand), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a page of expenses")
    @GetMapping("/{budgetName}")
    public ResponseEntity<Page<ExpenseDTO>> findAll(@PageableDefault Pageable pageable, @PathVariable("budgetName") String budgetName) {
        return new ResponseEntity<>(expenseService.findAllWithPagination(pageable, budgetName)
                .map(expense -> modelMapper
                        .map(expense, ExpenseDTO.class)), HttpStatus.OK);
    }
}
