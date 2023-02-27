package com.example.budgetmanager.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
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
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Budget implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @CreatedDate
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

    public Integer numberOfExpenses() {
        return expenses.size();
    }
}
