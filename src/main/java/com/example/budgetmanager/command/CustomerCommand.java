package com.example.budgetmanager.command;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomerCommand {
    @Size(min = 3, message = "Your name cannot be shorter than 3 letters!")
    private String name;
    @NotBlank(message = "Your login cannot be blank!")
    @Size(min = 5, message = "Your login cannot be shorter than 5 letters!")
    private String login;
    @Size(min = 5, max = 20, message = "Your password '${validatedValue}' must be between {min} and {max} characters long")
    private String password;
    @NotBlank(message = "Define your role (ADMIN/USER)")
    private String role;
}
