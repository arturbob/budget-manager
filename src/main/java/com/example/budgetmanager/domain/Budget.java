package com.example.budgetmanager.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@EqualsAndHashCode
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate createDate;
    private LocalDate expirationDate;
    private Long budgetSize;
    @CreatedBy
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @OneToMany(mappedBy = "budget")
    private Set<Expense> expenses = new HashSet<>();
}
