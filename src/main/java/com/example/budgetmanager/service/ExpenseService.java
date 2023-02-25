package com.example.budgetmanager.service;

import com.example.budgetmanager.domain.Budget;
import com.example.budgetmanager.domain.Expense;
import com.example.budgetmanager.exception.BudgetNotFound;
import com.example.budgetmanager.repository.BudgetRepository;
import com.example.budgetmanager.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;
    private final CustomUserDetailsService userDetailsService;

    public Expense save(Expense expense, String budgetName) {
        expense.setBudget(findBudgetForAddNewExpense(budgetName));
        return expenseRepository.save(expense);
    }

    public Budget findBudgetForAddNewExpense(String budgetName) {
        return budgetRepository.findBudgetByNameAndCustomer_Login(budgetName,
                        userDetailsService.getUserDetails().getUsername())
                .orElseThrow(BudgetNotFound::new);
    }
}
