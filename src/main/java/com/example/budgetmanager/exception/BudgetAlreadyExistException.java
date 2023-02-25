package com.example.budgetmanager.exception;

public class BudgetAlreadyExistException extends RuntimeException {
    public String getMessage(){
        return "You have already created budget with this name";
    }
}
