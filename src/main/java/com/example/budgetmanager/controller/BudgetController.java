package com.example.budgetmanager.controller;

import com.example.budgetmanager.command.BudgetCommand;
import com.example.budgetmanager.domain.Budget;
import com.example.budgetmanager.dto.BudgetDTO;
import com.example.budgetmanager.service.BudgetService;
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

@RequiredArgsConstructor
@RestController
@Tag(name = "Api for budget process", description = "Thanks to budget api we can create a new budget or got a list of ours")
@RequestMapping("/api/v1/budgets")
public class BudgetController {
    private final BudgetService budgetService;
    private final ModelMapper modelMapper;

    @Operation(summary = "Save a budget")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Budget created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BudgetDTO.class))})
    })
    @PostMapping
    public ResponseEntity<BudgetDTO> save(@RequestBody @Valid BudgetCommand budgetCommand) {
        return new ResponseEntity<>(modelMapper
                .map(budgetService
                        .save(modelMapper
                                .map(budgetCommand, Budget.class)), BudgetDTO.class), HttpStatus.CREATED);
    }
    @Operation(summary = "Get a budget")
    @GetMapping
    public ResponseEntity<Page<BudgetDTO>> listAll(@PageableDefault Pageable pageable) {
        return new ResponseEntity<>(budgetService.listAllWithPagination(pageable)
                .map(budget -> modelMapper
                        .map(budget, BudgetDTO.class)), HttpStatus.OK);
    }
}
