package com.example.budgetmanager.dto;

import com.example.budgetmanager.model.KindOfExpense;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExpenseDTO {
    private String name;
    private Long price;
    private LocalDateTime dateOfExpense;
    @Enumerated(EnumType.STRING)
    private KindOfExpense kindOfExpense;
}
