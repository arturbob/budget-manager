package com.example.budgetmanager.controller;

import com.example.budgetmanager.command.BudgetCommand;
import com.example.budgetmanager.domain.Budget;
import com.example.budgetmanager.domain.Customer;
import com.example.budgetmanager.model.Role;
import com.example.budgetmanager.repository.BudgetRepository;
import com.example.budgetmanager.repository.CustomerRepository;
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
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
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
public class BudgetControllerIT {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private ObjectMapper objectMapper;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BudgetRepository budgetRepository;
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        encoder = new BCryptPasswordEncoder();
        Customer customer = setUpAdmin();
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    public Customer setUpAdmin() {
        return customerRepository.save(Customer.builder()
                .name("Joe")
                .login("Admin")
                .password(encoder.encode("12345"))
                .role(Role.ADMIN)
                .budgets(Set.of())
                .enabled(true)
                .locked(false)
                .build());
    }

    public Customer setUpUser() {
        return customerRepository.save(Customer.builder()
                .name("Andrei")
                .login("User")
                .password(encoder.encode("12345"))
                .role(Role.USER)
                .budgets(Set.of())
                .enabled(true)
                .locked(false)
                .build());
    }

    @Test
    @WithUserDetails(value = "Admin", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userDetailsService")
    public void shouldSaveBudget() throws Exception {
        BudgetCommand budgetCommand = new BudgetCommand("February", LocalDate.of(2023, 10, 5), 1000.0);
        this.mockMvc.perform(post("/api/v1/budgets")
                        .content(objectMapper.writeValueAsString(budgetCommand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.budgetSize").value(1000.0));
        Assertions.assertTrue(budgetRepository.existsBudgetByNameAndCustomer_Login("February", "Admin"));
        Optional<Budget> savedBudget = budgetRepository.findById(1L);
        Assertions.assertTrue(savedBudget.isPresent());
        Assertions.assertEquals(LocalDate.now(), savedBudget.get().getCreateDate());
    }

    @Test
    public void shouldListAll() throws Exception {
        Customer admin = setUpAdmin();
        Customer user = setUpUser();
        budgetRepository.save(Budget.builder()
                .name("March")
                .createDate(LocalDate.now())
                .expirationDate(LocalDate.of(2023, 3, 20))
                .budgetSize(100.0)
                .customer(admin)
                .expenses(Set.of())
                .build());
        budgetRepository.save(Budget.builder()
                .name("April")
                .createDate(LocalDate.of(2023, 3, 21))
                .expirationDate(LocalDate.of(2023, 4, 20))
                .budgetSize(32125.0)
                .customer(admin)
                .expenses(Set.of())
                .build());
        budgetRepository.save(Budget.builder()
                .name("May")
                .createDate(LocalDate.of(2023, 4, 22))
                .expirationDate(LocalDate.of(2023, 5, 21))
                .budgetSize(15000.0)
                .customer(user)
                .expenses(Set.of())
                .build());
        this.mockMvc.perform(get("/api/v1/budgets")
                        .param("size", "1")
                        .param("page", "0")
                        .with(user(admin))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalElements").value(2));
    }
}
