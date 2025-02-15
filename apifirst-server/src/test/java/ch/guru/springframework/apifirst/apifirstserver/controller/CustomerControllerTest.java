package ch.guru.springframework.apifirst.apifirstserver.controller;

import ch.guru.springframework.apifirst.apifirstserver.repositories.CustomerRepository;
import ch.guru.springframework.apifirst.model.Address;
import ch.guru.springframework.apifirst.model.Customer;
import ch.guru.springframework.apifirst.model.Name;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Slf4j
class CustomerControllerTest {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    WebApplicationContext wac;
    
    @Autowired
    Filter validationFilter;

    @Autowired
    ObjectMapper objectMapper;

    public MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(validationFilter)
                .build();
    }

    @Test
    void testListCustomers() throws Exception {
        mockMvc.perform(get(CustomerController.CUSTOMER_BASE_URL)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", greaterThan(0)));
    }

    @Test
    void testGetCustomerById() throws Exception {
        Customer testCustomer = customerRepository.findAll().iterator().next();

        mockMvc.perform(get(CustomerController.CUSTOMER_BASE_URL + "/{customerId}", testCustomer.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(testCustomer.getId().toString()));
    }

    @Test
    void testCreateCustomer() throws Exception {
        Address address = Address.builder()
            .addressLine1("New Customer Address Line 1")
            .zip("12345")
            .state("ZH")
            .city("New Customer City")
            .build();
        
        Customer newCustomer = Customer.builder()
            .name(Name.builder()
                .firstName("New Customer Firstname")
                .lastName("New Customer Lastname")
                .build())
            .email("newcustomer@example.com")
            .phone("1234567890")
            .billToAddress(address)
            .shipToAddress(address)
            .build();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(CustomerController.CUSTOMER_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCustomer)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andReturn();

        String locationHeader = result.getResponse().getHeader("Location");
        log.info("Location header: {}", locationHeader);

        // Extract the URI from the location header
        URI locationUri = new URI(locationHeader);
        String path = locationUri.getPath();
        log.info("Extracted path: {}", path);

        // Perform GET request using the extracted path
        mockMvc.perform(get(path)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("newcustomer@example.com"));
    }
}
