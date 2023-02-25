package com.example.budgetmanager.dto;

import com.example.budgetmanager.model.Role;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomerDTO {
    private Long id;
    private String name;
    private String login;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Integer numberOfBudgets;
}
