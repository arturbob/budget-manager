package com.example.budgetmanager.repository;

import com.example.budgetmanager.domain.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomExpenseRepository {
    Page<Expense> findAll(Pageable pageable);
}