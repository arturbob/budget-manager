package com.example.budgetmanager.repository;

import com.example.budgetmanager.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByLogin(String login);
    boolean existsByLogin(String login);
}
