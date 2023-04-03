package com.example.budgetmanager.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerCommand {
    @Size(min = 3, message = "Your name cannot be shorter than 3 letters!")
    @Schema(minLength = 3, maxLength = 15, description = "Name of customer", example = "John")
    private String name;
    @NotBlank(message = "Your login cannot be blank!")
    @Size(min = 5, message = "Your login cannot be shorter than 5 letters!")
    @Schema(minLength = 5, maxLength = 15, description = "Login of customer", example = "login")
    private String login;
    @Size(min = 5, max = 20, message = "Your password '${validatedValue}' must be between {min} and {max} characters long")
    @Schema(minLength = 5, maxLength = 20, description = "Customer password", example = "password")
    private String password;
    @NotBlank(message = "Define your role (ADMIN/USER)")
    @Schema(minLength = 4, maxLength = 5, description = "Customer role", example = "USER", pattern = "ADMIN|USER")
    private String role;
}
