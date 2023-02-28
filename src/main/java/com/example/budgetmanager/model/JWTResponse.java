package com.example.budgetmanager.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JWTResponse {
    private String token;
}
