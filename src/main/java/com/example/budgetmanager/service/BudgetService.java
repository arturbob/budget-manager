package com.example.budgetmanager.service;

import com.example.budgetmanager.domain.Budget;
import com.example.budgetmanager.exception.BudgetAlreadyExistException;
import com.example.budgetmanager.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final CustomUserDetailsService userDetailsService;

    public Budget save(Budget budget) {
        if (budgetRepository.existsBudgetByNameAndCustomer_Login(budget.getName(),
                userDetailsService.getUserDetails().getUsername())) {
            throw new BudgetAlreadyExistException();
        }
        return budgetRepository.save(budget);
    }

    public Page<Budget> listAllWithPagination(Pageable pageable) {
        return budgetRepository.findBudgetsByCustomer_Login(userDetailsService.getUserDetails().getUsername(), pageable);
    }
}
