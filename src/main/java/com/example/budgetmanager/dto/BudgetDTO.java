package com.example.budgetmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Schema(name = "BudgetDTO", description = "Object BudgetDTO")
public class BudgetDTO {
    private String name;
    private LocalDate createDate;
    private LocalDate expirationDate;
    private Double budgetSize;
    private Double budgetLeft;
    private Integer numberOfExpenses;
}
