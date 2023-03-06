package com.example.budgetmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BudgetExpiredException extends RuntimeException {
    private final LocalDate dateOfExpired;

    public BudgetExpiredException(LocalDate dateOfExpired) {
        this.dateOfExpired = dateOfExpired;
    }

    public String getMessage() {
        return "Your budget has expired in " + dateOfExpired;
    }
}
