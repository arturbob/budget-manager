package com.example.budgetmanager.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerCommand {
    private String name;
    private String login;
    private String password;
    private String role;
}
