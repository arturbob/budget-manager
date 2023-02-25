package com.example.budgetmanager.service;

import com.example.budgetmanager.domain.Budget;
import com.example.budgetmanager.exception.BudgetAlreadyExistException;
import com.example.budgetmanager.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final CustomUserDetailsService userDetailsService;

    public Budget save(Budget budget) {
        if (budgetRepository.existsBudgetByName(budget.getName())) {
            throw new BudgetAlreadyExistException();
        }
        return budgetRepository.save(budget);
    }
    public List<Budget> listAll(){
        return budgetRepository.findBudgetByCustomer_Login(userDetailsService.getUserDetails().getUsername());
    }
}
