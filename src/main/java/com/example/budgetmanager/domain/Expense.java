package com.example.budgetmanager.domain;

import com.example.budgetmanager.model.KindOfExpense;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long price;
    private LocalDateTime dateOfExpense;
    private KindOfExpense kindOfExpense;
    @ManyToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;
}
