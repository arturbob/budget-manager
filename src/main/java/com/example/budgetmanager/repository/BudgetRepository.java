package com.example.budgetmanager.repository;

import com.example.budgetmanager.domain.Budget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findBudgetByNameAndCustomer_Login(String budgetName, String login);
    boolean existsBudgetByName(String name);
    Page<Budget> findBudgetsByCustomer_Login(String login, Pageable pageable);
}
