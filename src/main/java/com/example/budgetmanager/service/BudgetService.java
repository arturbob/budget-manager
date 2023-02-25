package com.example.budgetmanager.service;

import com.example.budgetmanager.domain.Budget;
import com.example.budgetmanager.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;

    public Budget save(Budget budget){
        return budgetRepository.save(budget);
    }
}
