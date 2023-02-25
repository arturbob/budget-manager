package com.example.budgetmanager.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BudgetDTO {
    private String name;
    private LocalDate createDate;
    private LocalDate expirationDate;
    private Long budgetSize;
    private Integer numberOfExpenses;
}
