package com.example.budgetmanager.model;

import com.example.budgetmanager.dto.ExpenseDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BudgetStatus {
    private ExpenseDTO expense;
    private Double budgetAmountLeft;
    private ValueOfExpense valueOfExpense;
}