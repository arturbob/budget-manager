package com.example.budgetmanager.repository;

import com.example.budgetmanager.domain.Budget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    @Query("SELECT b FROM Budget b LEFT JOIN FETCH b.expenses WHERE b.name = :budgetName AND b.customer.login = :login")
    Optional<Budget> findBudgetByNameAndCustomerLogin(String budgetName, String login);
    boolean existsBudgetByNameAndCustomer_Login(String name, String login);
    Page<Budget> findBudgetsByCustomer_Login(String login, Pageable pageable);
}
