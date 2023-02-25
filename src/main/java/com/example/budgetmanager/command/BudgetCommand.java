package com.example.budgetmanager.command;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BudgetCommand {
    private String name;
    private LocalDate expirationDate;
    private Long budgetSize;
}
