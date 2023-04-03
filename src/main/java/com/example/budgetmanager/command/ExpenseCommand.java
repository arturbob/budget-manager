package com.example.budgetmanager.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExpenseCommand {
    @Schema(minLength = 1, maxLength = 15, description = "Name of budget", example = "budget")
    private String budgetName;
    @NotBlank(message = "Name of your expense cannot be blank!")
    @Schema(minLength = 1, maxLength = 15, description = "Name of expense", example = "expense")
    private String name;
    @Min(1L)
    @NotNull(message = "Price of your expense cannot be null")
    @Schema(minLength = 1, maxLength = 999999, description = "Price of expense", example = "100")
    private Double price;
    @NotBlank(message = "Define kind of your expense (CRAVING/NEED)")
    @Schema(minLength = 4, maxLength = 7, description = "Kind of expense", example = "NEED", pattern = "CRAVING|NEED")
    private String kindOfExpense;
}
