package com.example.budgetmanager.model;

import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ApiError {
    private HttpStatus status;
    private Object errors;
}
