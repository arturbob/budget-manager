package com.example.budgetmanager.controller;

import com.example.budgetmanager.command.BudgetCommand;
import com.example.budgetmanager.domain.Budget;
import com.example.budgetmanager.domain.Customer;
import com.example.budgetmanager.model.AuthenticationRequest;
import com.example.budgetmanager.model.Role;
import com.example.budgetmanager.repository.BudgetRepository;
import com.example.budgetmanager.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("application-tests")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
public class AuthControllerIT {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BudgetRepository budgetRepository;

    @BeforeEach
    public void setup() {
        Customer customer = setUpUser();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    public Customer setUpUser() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return customerRepository.save(Customer.builder()
                .name("Andrei")
                .login("admin")
                .password(bCryptPasswordEncoder.encode("DKJSAJKGdkqjk23"))
                .role(Role.ADMIN)
                .budgets(Set.of())
                .enabled(true)
                .locked(false)
                .build());
    }

    @Test
    void shouldAuthenticate() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("admin", "DKJSAJKGdkqjk23");
        this.mockMvc.perform(post("/api/v1/authentication")
                        .content(objectMapper.writeValueAsString(authenticationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldAuthenticateThenReturnBudgets() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("admin", "DKJSAJKGdkqjk23");
        String responseJson = this.mockMvc.perform(post("/api/v1/authentication")
                        .content(objectMapper.writeValueAsString(authenticationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String jwt = JsonPath.read(responseJson, "$.token");
        BudgetCommand budgetCommand = new BudgetCommand("February", LocalDate.of(2023, 10, 5), 1000.0);
        this.mockMvc.perform(post("/api/v1/budgets")
                        .header("Authorization", "Bearer " + jwt)
                        .content(objectMapper.writeValueAsString(budgetCommand))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.budgetSize").value(1000.0));
        Assertions.assertTrue(budgetRepository.existsBudgetByNameAndCustomer_Login("February", "Admin"));
        Optional<Budget> savedBudget = budgetRepository.findById(1L);
        Assertions.assertTrue(savedBudget.isPresent());
        Assertions.assertEquals(LocalDate.now(), savedBudget.get().getCreateDate());
    }
}
