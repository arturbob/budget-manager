package com.example.budgetmanager.command;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(style = "yyyy-MM-dd")
    private LocalDate expirationDate;
    private Long budgetSize;
}
