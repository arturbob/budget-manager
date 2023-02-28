package com.example.budgetmanager.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExpenseSearchFilter {
    private Double priceFrom;
    private Double priceTo;
    private LocalDate dateOfExpenseFrom;
    private LocalDate dateOfExpenseTo;
    private LocalDate dateOfExpense;
    private String kindOfExpense;
}