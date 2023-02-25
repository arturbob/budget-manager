package com.example.budgetmanager.command;

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
    @NotBlank(message = "Your name cannot be blank!")
    @Size(min = 3, message = "Your name cannot be smaller than 3 letters!")
    private String name;
    @NotBlank
    @Size(min = 5, message = "Your login cannot be smaller than 5 letters!")
    private String login;
    @NotBlank
    @Size(min = 5, max = 20, message = "Your password must be between 5 and 20")
    private String password;
    @NotBlank(message = "Define your role (ADMIN/USER)")
    private String role;
}
