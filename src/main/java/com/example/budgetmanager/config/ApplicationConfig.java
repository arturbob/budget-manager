package com.example.budgetmanager.config;

import com.example.budgetmanager.domain.Budget;
import com.example.budgetmanager.domain.Customer;
import com.example.budgetmanager.dto.BudgetDTO;
import com.example.budgetmanager.dto.CustomerDTO;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap(Customer.class, CustomerDTO.class)
                .addMappings(mapper -> mapper
                        .map(Customer::numberOfBudgets, CustomerDTO::setNumberOfBudgets));
        modelMapper.createTypeMap(Budget.class, BudgetDTO.class)
                .addMappings(mapper -> mapper
                        .map(Budget::numberOfExpenses, BudgetDTO::setNumberOfExpenses));
        return modelMapper;
    }
}
