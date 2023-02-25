package com.example.budgetmanager.command;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExpenseCommand {
    private String name;
    private Long price;
    private LocalDateTime dateOfExpense;
    private String kindOfExpense;
    private String budgetName;
}
