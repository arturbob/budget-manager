package com.example.budgetmanager.model;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JWTRequest {
    private String login;
    private String password;
}
