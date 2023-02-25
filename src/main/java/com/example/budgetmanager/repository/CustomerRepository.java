package com.example.budgetmanager.repository;

import com.example.budgetmanager.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
