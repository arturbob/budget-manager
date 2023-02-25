package com.example.budgetmanager.command;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BudgetCommand {
    @NotBlank(message = "Your feature name of budget cannot be blank!")
    private String name;
    @NotBlank(message = "Date of expiration cannot be blank, use patter YYYY-MM-DD")
    private LocalDate expirationDate;
    @NotBlank(message = "Define your budget size!")
    private Long budgetSize;
}
