package com.example.budgetmanager.controller;

import com.example.budgetmanager.command.ExpenseCommand;
import com.example.budgetmanager.domain.Budget;
import com.example.budgetmanager.domain.Customer;
import com.example.budgetmanager.domain.Expense;
import com.example.budgetmanager.model.KindOfExpense;
import com.example.budgetmanager.model.Role;
import com.example.budgetmanager.repository.BudgetRepository;
import com.example.budgetmanager.repository.CustomerRepository;
import com.example.budgetmanager.repository.ExpenseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("application-tests")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
public class ExpenseControllerIT {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    private BCryptPasswordEncoder encoder;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        encoder = new BCryptPasswordEncoder();
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    public Customer setUpAdmin() {
        return customerRepository.save(Customer.builder()
                .name("Andrei")
                .login("Admin")
                .password(encoder.encode("12345"))
                .role(Role.ADMIN)
                .budgets(Set.of())
                .enabled(true)
                .locked(false)
                .build());
    }

    @Test
    public void shouldSaveExpense() throws Exception {
        Customer customer = setUpAdmin();
        budgetRepository.save(Budget.builder()
                .name("MARCH")
                .createDate(LocalDate.now())
                .expirationDate(LocalDate.of(2023, 3, 20))
                .budgetSize(100.0)
                .budgetLeft(100.0)
                .customer(customer)
                .expenses(Set.of())
                .build());
        Optional<Budget> budget = budgetRepository.findBudgetByNameAndCustomerLogin("MARCH", customer.getLogin());
        Assertions.assertTrue(budget.isPresent());
        ExpenseCommand expenseCommand = new ExpenseCommand("MARCH", "Apple", 1.0, "NEED");
        this.mockMvc.perform(post("/api/v1/expenses")
                        .with(user(customer))
                        .content(objectMapper.writeValueAsString(expenseCommand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.valueOfExpense").value("LOW"))
                .andExpect(jsonPath("$.expense.name").value("Apple"))
                .andExpect(jsonPath("$.expense.price").value(1.0));

        Optional<Expense> savedExpense = expenseRepository.findById(1L);
        Assertions.assertTrue(savedExpense.isPresent());

        Assertions.assertEquals(1.0, savedExpense.get().getPrice());
        Assertions.assertEquals("Apple", savedExpense.get().getName());
        Assertions.assertEquals(KindOfExpense.NEED, savedExpense.get().getKindOfExpense());
    }

    @Test
    public void shouldListAll() throws Exception {
        Customer admin = setUpAdmin();
        Budget budget = budgetRepository.saveAndFlush(Budget.builder()
                .name("March")
                .createDate(LocalDate.now())
                .expirationDate(LocalDate.of(2023, 3, 20))
                .budgetSize(100.0)
                .budgetLeft(100.0)
                .customer(admin)
                .expenses(Set.of())
                .build());
        expenseRepository.saveAndFlush(Expense.builder()
                .name("Bread")
                .budget(budget)
                .dateOfExpense(LocalDate.now())
                .kindOfExpense(KindOfExpense.NEED)
                .price(10.0)
                .build());
        expenseRepository.saveAndFlush(Expense.builder()
                .name("Lamp")
                .budget(budget)
                .dateOfExpense(LocalDate.now())
                .kindOfExpense(KindOfExpense.CRAVING)
                .price(15.0)
                .build());
        expenseRepository.saveAndFlush(Expense.builder()
                .name("Shoes")
                .budget(budget)
                .dateOfExpense(LocalDate.now())
                .kindOfExpense(KindOfExpense.CRAVING)
                .price(121.0)
                .build());
        this.mockMvc.perform(get("/api/v1/expenses/{budgetName}", "March")
                        .param("size", "1")
                        .param("page", "0")
                        .with(user(admin))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalElements").value(3));
    }

}
