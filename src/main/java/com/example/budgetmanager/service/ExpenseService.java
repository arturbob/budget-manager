package com.example.budgetmanager.service;

import com.example.budgetmanager.command.ExpenseCommand;
import com.example.budgetmanager.domain.Budget;
import com.example.budgetmanager.model.BudgetStatus;
import com.example.budgetmanager.domain.Expense;
import com.example.budgetmanager.dto.ExpenseDTO;
import com.example.budgetmanager.exception.BudgetExceededException;
import com.example.budgetmanager.exception.BudgetNotFoundException;
import com.example.budgetmanager.model.KindOfExpense;
import com.example.budgetmanager.model.ValueOfExpense;
import com.example.budgetmanager.repository.BudgetRepository;
import com.example.budgetmanager.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;
    private final CustomUserDetailsService userDetailsService;
    private final ModelMapper modelMapper;

    @Transactional
    public BudgetStatus save(ExpenseCommand expenseCommand) {
        Expense expense = buildAndSave(expenseCommand);
        Budget budget = findBudgetForAuthenticatedCustomer(expenseCommand.getBudgetName());
        Double budgetSizeBeforeExpense = budget.getBudgetLeft();
        Double budgetSizeAfterExpense = addExpenseToBudget(budget, expense);
        return buildBudgetStatus(expense, budget, budgetSizeBeforeExpense, budgetSizeAfterExpense);
    }

//    @Transactional
    public Expense buildAndSave(ExpenseCommand expenseCommand) {
        Budget budget = findBudgetForAuthenticatedCustomer(expenseCommand.getBudgetName());
        return expenseRepository.save(Expense.builder()
                .name(expenseCommand.getName())
                .dateOfExpense(LocalDate.now())
                .kindOfExpense(KindOfExpense.valueOf(expenseCommand.getKindOfExpense()))
                .price(expenseCommand.getPrice())
                .budget(budget)
                .build());
    }

    public BudgetStatus buildBudgetStatus(Expense expense, Budget budget, Double budgetSizeBeforeExpense, Double budgetSizeAfterExpense) {
        return BudgetStatus.builder()
                .expense(modelMapper.map(expense, ExpenseDTO.class))
                .budgetAmountLeft(budget.getBudgetLeft())
                .valueOfExpense(valueOfExpense(budgetSizeBeforeExpense, budgetSizeAfterExpense))
                .build();
    }
    @Transactional
    public Double addExpenseToBudget(Budget budget, Expense expense) {
        budget.getExpenses().add(expense);
        budget.setBudgetLeft(budget.getBudgetLeft() - expense.getPrice());
        if (budget.getBudgetLeft() < 0) {
            throw new BudgetExceededException(budget.getBudgetLeft());
        }
        budgetRepository.save(budget);
        return budget.getBudgetLeft();
    }

    public ValueOfExpense valueOfExpense(Double beforeExpense, Double afterExpense) {
        Double value = beforeExpense - afterExpense;
        if (value / beforeExpense <= 0.15) {
            return ValueOfExpense.LOW;
        } else if (value / beforeExpense > 0.15 && value / beforeExpense <= 0.35) {
            return ValueOfExpense.AVERAGE;
        } else {
            return ValueOfExpense.HIGH;
        }
    }

    public Budget findBudgetForAuthenticatedCustomer(String budgetName) {
        return budgetRepository.findBudgetByNameAndCustomerLogin(budgetName,
                        userDetailsService.getUserDetails().getUsername())
                .orElseThrow(BudgetNotFoundException::new);
    }

    public Page<Expense> findAllWithPagination(Pageable pageable, String budgetName) {
        Budget budget = findBudgetForAuthenticatedCustomer(budgetName);
        return expenseRepository.findAllByBudget(budget, pageable);
    }
}
