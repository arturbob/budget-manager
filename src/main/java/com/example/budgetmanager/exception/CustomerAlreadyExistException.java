package com.example.budgetmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomerAlreadyExistException extends RuntimeException {
    public String getMessage() {
        return "Customer with this login already exist, choose different!";
    }
}
