package com.example.budgetmanager.repository;

import com.example.budgetmanager.domain.Expense;
import com.example.budgetmanager.domain.QExpense;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class ExpenseFilterRepository extends QuerydslRepositorySupport implements CustomExpenseRepository {

    public ExpenseFilterRepository() {
        super(Expense.class);
    }

    @Override
    public Page<Expense> findAll(Pageable pageable) {
        QExpense expense = QExpense.expense;
        JPQLQuery<Expense> query = from(expense);



        return PageableExecutionUtils
                .getPage(Objects.requireNonNull(getQuerydsl())
                        .applyPagination(pageable, query).fetch(), pageable, query::fetchCount);
    }
}