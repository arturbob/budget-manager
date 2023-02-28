package com.example.budgetmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BudgetExceededException extends RuntimeException{
    private final Double value;

    public BudgetExceededException(Double value) {
        this.value = value;
    }

    public String getMessage(){
        return "Your budget has been exceeded by " + value;
    }
}