package com.example.budgetmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BudgetNotFoundException extends RuntimeException{
    public String getMessage(){
        return "Budget with this name does not exist!";
    }
}
