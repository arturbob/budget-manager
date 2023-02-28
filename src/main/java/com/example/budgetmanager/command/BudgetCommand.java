package com.example.budgetmanager.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BudgetCommand {
    @NotBlank(message = "Your feature name of budget cannot be blank!")
    private String name;
    @DateTimeFormat(style = "yyyy-MM-dd")
    private LocalDate expirationDate;
    @Min(1L)
    @NotNull(message = "Your budget cannot be null")
    private Double budgetSize;
}
