package com.example.budgetmanager.command;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExpenseCommand {
    @NotBlank(message = "Name of your expense cannot be blank!")
    private String name;
    @NotBlank(message = "Price of your expense cannot be blank!")
    private Long price;
    @NotBlank(message = "Define kind of your expense (CRAVING/NEED)")
    private String kindOfExpense;
}
