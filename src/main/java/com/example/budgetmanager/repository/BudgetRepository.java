package com.example.budgetmanager.repository;

import com.example.budgetmanager.domain.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
}
