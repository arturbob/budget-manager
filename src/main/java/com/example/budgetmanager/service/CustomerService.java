package com.example.budgetmanager.service;

import com.example.budgetmanager.domain.Customer;
import com.example.budgetmanager.exception.CustomerAlreadyExistException;
import com.example.budgetmanager.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Customer save(Customer customer) {
        if (customerRepository.existsByLogin(customer.getLogin())) {
            throw new CustomerAlreadyExistException();
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }
}
