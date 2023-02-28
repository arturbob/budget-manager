package com.example.budgetmanager.domain;

import com.example.budgetmanager.model.KindOfExpense;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Builder
public class Expense implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    @CreatedDate
    private LocalDate dateOfExpense;
    @Enumerated(EnumType.STRING)
    private KindOfExpense kindOfExpense;
    @ManyToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;
}
