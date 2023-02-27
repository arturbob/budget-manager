package com.example.budgetmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BudgetAlreadyExistException extends RuntimeException {
    public String getMessage(){
        return "You have already created budget with this name";
    }
}
