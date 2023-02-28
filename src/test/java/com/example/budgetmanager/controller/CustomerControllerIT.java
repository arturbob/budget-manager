package com.example.budgetmanager.controller;

import com.example.budgetmanager.command.CustomerCommand;
import com.example.budgetmanager.domain.Customer;
import com.example.budgetmanager.model.Role;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("application-tests")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
public class CustomerControllerIT {

    private MockMvc mockMvc;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private ObjectMapper objectMapper;
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    public void setUp() {
        encoder = new BCryptPasswordEncoder();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void shouldSaveCustomer() throws Exception {
        CustomerCommand customerCommand = new CustomerCommand("John", "Joe123", "password123", "USER");
        this.mockMvc.perform(post("/api/v1/customers")
                        .content(objectMapper.writeValueAsString(customerCommand))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.login").value("Joe123"));

        Optional<Customer> savedCustomer = customerRepository.findByLogin(customerCommand.getLogin());
        Assertions.assertTrue(savedCustomer.isPresent());
        Customer customer = savedCustomer.get();
        Assertions.assertEquals("Joe123", customer.getLogin());
        Assertions.assertTrue(encoder.matches("password123", customer.getPassword()));
        Assertions.assertEquals(Role.USER, customer.getRole());
    }
}
