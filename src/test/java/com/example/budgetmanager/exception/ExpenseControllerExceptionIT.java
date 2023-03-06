package com.example.budgetmanager.exception;

import com.example.budgetmanager.command.ExpenseCommand;
import com.example.budgetmanager.domain.Budget;
import com.example.budgetmanager.domain.Customer;
import com.example.budgetmanager.model.Role;
import com.example.budgetmanager.repository.BudgetRepository;
import com.example.budgetmanager.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("application-tests")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
public class ExpenseControllerExceptionIT {
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

    @Test
    public void shouldThrowBudgetNotFoundException() throws Exception {
        Customer admin = setUpAdmin();
        budgetRepository.saveAndFlush(Budget.builder()
                .name("March")
                .createDate(LocalDate.now())
                .expirationDate(LocalDate.of(2023, 3, 20))
                .budgetSize(100.0)
                .budgetLeft(100.0)
                .customer(admin)
                .expenses(Set.of())
                .build());
        ExpenseCommand expenseCommand = new ExpenseCommand("", "Bread", 12.0, "NEED");
        this.mockMvc.perform(post("/api/v1/expenses")
                        .with(user(admin))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseCommand))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertEquals("NOT_FOUND",
                        (JsonPath.read(result.getResponse().getContentAsString(), "$.status"))))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> Assertions.assertEquals("Budget with this name does not exist!",
                        (JsonPath.read(result.getResponse()
                                .getContentAsString(), "$.errors"))));
    }

    @Test
    public void shouldThrowValidationMessageWhenSaveExpenseWithBlankPrice() throws Exception {
        Customer admin = setUpAdmin();
        budgetRepository.saveAndFlush(Budget.builder()
                .name("March")
                .createDate(LocalDate.now())
                .expirationDate(LocalDate.of(2023, 3, 20))
                .budgetSize(100.0)
                .budgetLeft(100.0)
                .customer(admin)
                .expenses(Set.of())
                .build());
        ExpenseCommand expenseCommand = new ExpenseCommand("March", "Bread", null, "NEED");
        this.mockMvc.perform(post("/api/v1/expenses")
                        .with(user(admin))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseCommand))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertEquals("BAD_REQUEST",
                        (JsonPath.read(result.getResponse().getContentAsString(), "$.status"))))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> Assertions.assertEquals("Price of your expense cannot be null",
                        (JsonPath.read(result.getResponse()
                                .getContentAsString(), "$.errors.price"))));
    }

    @Test
    public void shouldThrowValidationMessageWhenSaveExpenseWithBlankKindOfExpense() throws Exception {
        Customer admin = setUpAdmin();
        budgetRepository.saveAndFlush(Budget.builder()
                .name("March")
                .createDate(LocalDate.now())
                .expirationDate(LocalDate.of(2023, 3, 20))
                .budgetSize(100.0)
                .budgetLeft(100.0)
                .customer(admin)
                .expenses(Set.of())
                .build());
        ExpenseCommand expenseCommand = new ExpenseCommand("March", "Bread", 12.0, "");
        this.mockMvc.perform(post("/api/v1/expenses")
                        .with(user(admin))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseCommand))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertEquals("BAD_REQUEST",
                        (JsonPath.read(result.getResponse().getContentAsString(), "$.status"))))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> Assertions.assertEquals("Define kind of your expense (CRAVING/NEED)",
                        (JsonPath.read(result.getResponse()
                                .getContentAsString(), "$.errors.kindOfExpense"))));
    }
}
