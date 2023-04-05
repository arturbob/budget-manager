package com.example.budgetmanager.dto;

import com.example.budgetmanager.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Schema(name = "CustomerDTO", description = "Object CustomerDTO")
public class CustomerDTO {
    private Long id;
    private String name;
    private String login;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Integer numberOfBudgets;
}
