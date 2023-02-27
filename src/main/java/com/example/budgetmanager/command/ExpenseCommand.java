package com.example.budgetmanager.command;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExpenseCommand {
    private String budgetName;
    @NotBlank(message = "Name of your expense cannot be blank!")
    private String name;
    @Min(1L)
    @NotNull(message = "Price of your expense cannot be null")
    private Long price;
    @NotBlank(message = "Define kind of your expense (CRAVING/NEED)")
    private String kindOfExpense;
}
