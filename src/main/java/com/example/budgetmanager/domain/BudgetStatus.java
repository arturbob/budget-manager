package com.example.budgetmanager.domain;

import com.example.budgetmanager.dto.ExpenseDTO;
import com.example.budgetmanager.model.ValueOfExpense;
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