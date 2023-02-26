package com.example.budgetmanager.repository;

import com.example.budgetmanager.domain.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Page<Expense> findAllByBudget_Name(String name, Pageable pageable);
}
