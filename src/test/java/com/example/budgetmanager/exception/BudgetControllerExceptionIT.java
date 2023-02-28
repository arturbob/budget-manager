package com.example.budgetmanager.exception;

import com.example.budgetmanager.command.BudgetCommand;
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
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
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
public class BudgetControllerExceptionIT {
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

    @Test
    @WithUserDetails(value = "Admin", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userDetailsService")
    public void shouldThrowValidationMessageWhenSaveBudgetWithNullBudgetSize() throws Exception {
        BudgetCommand budgetCommand = new BudgetCommand("February", LocalDate.of(2023, 10, 5), null);
        this.mockMvc.perform(post("/api/v1/budgets")
                        .content(objectMapper.writeValueAsString(budgetCommand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> Assertions.assertEquals("Your budget cannot be null",
                        (JsonPath.read(result.getResponse()
                                .getContentAsString(), "$.errors.budgetSize"))))
                .andExpect(result -> Assertions.assertEquals("BAD_REQUEST",
                        JsonPath.read(result.getResponse()
                                .getContentAsString(), "$.status")));
    }

    @Test
    @WithUserDetails(value = "Admin", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "userDetailsService")
    public void shouldThrowValidationMessageWhenSaveBudgetWithBlankName() throws Exception {
        BudgetCommand budgetCommand = new BudgetCommand("", LocalDate.of(2023, 10, 5), 1000.0);
        this.mockMvc.perform(post("/api/v1/budgets")
                        .content(objectMapper.writeValueAsString(budgetCommand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> Assertions.assertEquals("Your feature name of budget cannot be blank!",
                        (JsonPath.read(result.getResponse()
                                .getContentAsString(), "$.errors.name"))))
                .andExpect(result -> Assertions.assertEquals("BAD_REQUEST",
                        JsonPath.read(result.getResponse()
                                .getContentAsString(), "$.status")));
    }

    @Test
    public void shouldThrowBudgetAlreadyExistExceptionMessageWhenSaveBudget() throws Exception {
        Customer customer = setUpAdmin();
        budgetRepository.saveAndFlush(Budget.builder()
                .name("March")
                .createDate(LocalDate.now())
                .expirationDate(LocalDate.of(2023, 3, 20))
                .budgetSize(100.0)
                .customer(customer)
                .expenses(Set.of())
                .build());
        BudgetCommand budgetCommand = new BudgetCommand("March", LocalDate.of(2023, 10, 5), 1000.0);
        this.mockMvc.perform(post("/api/v1/budgets")
                        .with(user(customer))
                        .content(objectMapper.writeValueAsString(budgetCommand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> Assertions.assertEquals("You have already created budget with this name",
                        (JsonPath.read(result.getResponse().getContentAsString(), "$.errors"))))
                .andExpect(result -> Assertions.assertEquals("BAD_REQUEST",
                        (JsonPath.read(result.getResponse().getContentAsString(), "$.status"))));
    }

}
