package com.example.budgetmanager.repository;

import com.example.budgetmanager.domain.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
