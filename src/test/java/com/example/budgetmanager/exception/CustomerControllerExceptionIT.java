package com.example.budgetmanager.exception;

import com.example.budgetmanager.command.CustomerCommand;
import com.example.budgetmanager.domain.Customer;
import com.example.budgetmanager.model.Role;
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

import java.util.Set;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("application-tests")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
public class CustomerControllerExceptionIT {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private ObjectMapper objectMapper;
    @Autowired
    private CustomerRepository customerRepository;
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

    public Customer setUpCustomer() {
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
    public void shouldThrowValidationMessageWhenSaveCustomerWithToShortName() throws Exception {
        CustomerCommand customerCommand = new CustomerCommand("he", "Kolba", "12345", "ADMIN");
        this.mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerCommand))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertEquals("BAD_REQUEST",
                        (JsonPath.read(result.getResponse().getContentAsString(), "$.status"))))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> Assertions.assertEquals("Your name cannot be shorter than 3 letters!",
                        (JsonPath.read(result.getResponse()
                                .getContentAsString(), "$.errors.name"))));
    }

    @Test
    public void shouldThrowValidationMessageWhenSaveCustomerWithTooShortLogin() throws Exception {
        CustomerCommand customerCommand = new CustomerCommand("Angelica", "Ang", "12345", "USER");
        this.mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerCommand))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertEquals("BAD_REQUEST",
                        (JsonPath.read(result.getResponse().getContentAsString(), "$.status"))))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> Assertions.assertEquals("Your login cannot be shorter than 5 letters!",
                        (JsonPath.read(result.getResponse()
                                .getContentAsString(), "$.errors.login"))));
    }

    @Test
    public void shouldThrowValidationMessageWhenSaveCustomerWithTooShortPassword() throws Exception {
        CustomerCommand customerCommand = new CustomerCommand("John", "Johny", "123", "USER");
        this.mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerCommand))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertEquals("BAD_REQUEST",
                        (JsonPath.read(result.getResponse().getContentAsString(), "$.status"))))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> Assertions.assertEquals("Your password '123' must be between 5 and 20 characters long",
                        (JsonPath.read(result.getResponse()
                                .getContentAsString(), "$.errors.password"))));
    }

    @Test
    public void shouldThrowValidationMessageWhenSaveCustomerWithWrongRole() throws Exception {
        CustomerCommand customerCommand = new CustomerCommand("John", "Johny", "12345", "");
        this.mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerCommand))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertEquals("BAD_REQUEST",
                        (JsonPath.read(result.getResponse().getContentAsString(), "$.status"))))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> Assertions.assertEquals("Define your role (ADMIN/USER)",
                        (JsonPath.read(result.getResponse()
                                .getContentAsString(), "$.errors.role"))));
    }

    @Test
    public void shouldThrowCustomerAlreadyExistExceptionWhenSaveCustomerWithConflictLogin() throws Exception {
        Customer customer = setUpCustomer();
        CustomerCommand customerCommand = new CustomerCommand("Joe", "Admin", "12345", "ADMIN");
        this.mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerCommand))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> Assertions.assertEquals("Customer with this login already exist, choose different!",
                        (JsonPath.read(result.getResponse()
                                .getContentAsString(), "$.errors"))))
                .andExpect(result -> Assertions.assertEquals("BAD_REQUEST",
                        JsonPath.read(result.getResponse()
                                .getContentAsString(), "$.status")));
    }
}
