package com.example.budgetmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BudgetNotFoundException extends RuntimeException {
    public String getMessage() {
        return "Budget with this name does not exist!";
    }
}
