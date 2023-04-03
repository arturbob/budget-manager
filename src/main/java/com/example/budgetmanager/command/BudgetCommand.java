package com.example.budgetmanager.command;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(minLength = 1, maxLength = 15, description = "Name of budget", example = "name")
    private String name;
    @DateTimeFormat(style = "yyyy-MM-dd")
    @Schema(description = "Expiration date of budget", example = "2000-01-01")
    private LocalDate expirationDate;
    @Min(1L)
    @NotNull(message = "Your budget cannot be null")
    @Schema(minLength = 1, maxLength = 999999, description = "Size of budget", example = "100")
    private Double budgetSize;
}
