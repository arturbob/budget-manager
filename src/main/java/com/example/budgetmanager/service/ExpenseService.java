package com.example.budgetmanager.service;

import com.example.budgetmanager.command.ExpenseCommand;
import com.example.budgetmanager.domain.Budget;
import com.example.budgetmanager.domain.Expense;
import com.example.budgetmanager.exception.BudgetNotFoundException;
import com.example.budgetmanager.model.KindOfExpense;
import com.example.budgetmanager.repository.BudgetRepository;
import com.example.budgetmanager.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;
    private final CustomUserDetailsService userDetailsService;

    public Expense save(ExpenseCommand expenseCommand) {
        Budget budget = findBudgetForAddNewExpense(expenseCommand.getBudgetName());
        Expense expense = Expense.builder()
                .name(expenseCommand.getName())
                .dateOfExpense(LocalDateTime.now())
                .kindOfExpense(KindOfExpense.valueOf(expenseCommand.getKindOfExpense()))
                .price(expenseCommand.getPrice())
                .budget(budget)
                .build();
        expenseRepository.save(expense);
        budget.getExpenses().add(expense);
        return expense;
    }


    public Budget findBudgetForAddNewExpense(String budgetName) {
        return budgetRepository.findBudgetByNameAndCustomer_Login(budgetName,
                        userDetailsService.getUserDetails().getUsername())
                .orElseThrow(BudgetNotFoundException::new);
    }

    public Page<Expense> findAllWithPagination(Pageable pageable, String budgetName) {
        return expenseRepository.findAllByBudget_Name(budgetName, pageable);
    }
}
