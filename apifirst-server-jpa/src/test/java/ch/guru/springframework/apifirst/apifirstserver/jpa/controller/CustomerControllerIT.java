package ch.guru.springframework.apifirst.apifirstserver.jpa.controller;

import ch.guru.springframework.apifirst.apifirstserver.jpa.bootstrap.DataLoader;
import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Customer;
import ch.guru.springframework.apifirst.apifirstserver.jpa.mapper.CustomerMapper;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.CustomerRepository;
import ch.guru.springframework.apifirst.model.*;
import jakarta.servlet.Filter;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.core.StreamReadFeature;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.Collections;
import java.util.UUID;

import static ch.guru.springframework.apifirst.apifirstserver.jpa.config.OpenApiValidationConfig.OPENAPI_SPECIFICATION_URL;
import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(DataLoader.class)
@Slf4j
class CustomerControllerIT {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    Filter validationFilter;

    @Autowired
    ObjectMapper objectMapper;

    public MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper = objectMapper.rebuild()
            .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
            .build();
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .addFilter(validationFilter)
            .build();
    }

    @Test
    @Order(1)
    void testListCustomers() throws Exception {
        mockMvc.perform(get(CustomerController.CUSTOMER_BASE_URL)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", greaterThan(0)))
            .andExpect(openApi().isValid(OPENAPI_SPECIFICATION_URL));
    }

    @Test
    @Order(2)
    void testGetCustomerById() throws Exception {
        Customer testCustomer = customerRepository.findAll().getFirst();

        mockMvc.perform(get(CustomerController.CUSTOMER_BASE_URL + "/{customerId}", testCustomer.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(testCustomer.getId().toString()))
            .andExpect(openApi().isValid(OPENAPI_SPECIFICATION_URL));
    }

    @Test
    @Order(2)
    void testGetCustomerByIdNotFound() throws Exception {
        mockMvc.perform(get(CustomerController.CUSTOMER_BASE_URL + "/{customerId}", UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(openApi().isValid(OPENAPI_SPECIFICATION_URL));
    }

    @Test
    @Order(3)
    void testCreateCustomer() throws Exception {
        AddressDto address = AddressDto.builder()
            .addressLine1("New Customer Address Line 1")
            .zip("12345")
            .state("ZH")
            .city("New Customer City")
            .build();

        CustomerDto newCustomer = CustomerDto.builder()
            .name(NameDto.builder()
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
            .andExpect(openApi().isValid(OPENAPI_SPECIFICATION_URL))
            .andReturn();

        String locationHeader = result.getResponse().getHeader("Location");
        log.info("Location header: {}", locationHeader);
        assertNotNull(locationHeader);

        // Extract the URI from the location header
        URI locationUri = new URI(locationHeader);
        String path = locationUri.getPath();
        log.info("Extracted path: {}", path);

        // Perform GET request using the extracted path
        mockMvc.perform(get(path)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("newcustomer@example.com"))
            .andExpect(openApi().isValid(OPENAPI_SPECIFICATION_URL));
    }

    @Transactional
    @Test
    void testUpdateCustomer() throws Exception {
        Customer customer = customerRepository.findAll().getFirst();

        customer.getName().setFirstName("Updated");
        customer.getName().setLastName("Updated2");
        customer.getPaymentMethods().getFirst().setDisplayName("NEW NAME");

        mockMvc.perform(put(CustomerController.CUSTOMER_BASE_URL + "/{customerId}", customer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerMapper.customerToDto(customer))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name.firstName", equalTo("Updated")))
            .andExpect(jsonPath("$.name.lastName", equalTo("Updated2")))
            .andExpect(jsonPath("$.paymentMethods[0].displayName", equalTo("NEW NAME")))
            .andExpect(openApi().isValid(OPENAPI_SPECIFICATION_URL));
    }

    @Transactional
    @Test
    void testUpdateCustomerNotFound() throws Exception {
        Customer customer = customerRepository.findAll().getFirst();

        customer.getName().setFirstName("Updated");
        customer.getName().setLastName("Updated2");
        customer.getPaymentMethods().getFirst().setDisplayName("NEW NAME");

        mockMvc.perform(put(CustomerController.CUSTOMER_BASE_URL + "/{customerId}", UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerMapper.customerToDto(customer))))
            .andExpect(status().isNotFound())
            .andExpect(openApi().isValid(OPENAPI_SPECIFICATION_URL));
    }

    @Transactional
    @Test
    void testPatchCustomer() throws Exception {
        Customer customer = customerRepository.findAll().getFirst();

        CustomerPatchDto customerPatch = CustomerPatchDto.builder()
            .name(CustomerNamePatchDto.builder()
                .firstName("Updated")
                .lastName("Updated2")
                .build())
            .paymentMethods(Collections.singletonList(CustomerPaymentMethodPatchDto.builder()
                .id(customer.getPaymentMethods().getFirst().getId())
                .displayName("NEW NAME")
                .build()))
            .build();

        mockMvc.perform(patch(CustomerController.CUSTOMER_BASE_URL + "/{customerId}", customer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerPatch)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name.firstName", equalTo("Updated")))
            .andExpect(jsonPath("$.name.lastName", equalTo("Updated2")))
            .andExpect(jsonPath("$.paymentMethods[0].displayName", equalTo("NEW NAME")))
            .andExpect(openApi().isValid(OPENAPI_SPECIFICATION_URL));
    }

    @Transactional
    @Test
    void testPatchCustomerNotFound() throws Exception {
        Customer customer = customerRepository.findAll().getFirst();

        CustomerPatchDto customerPatch = CustomerPatchDto.builder()
            .name(CustomerNamePatchDto.builder()
                .firstName("Updated")
                .lastName("Updated2")
                .build())
            .paymentMethods(Collections.singletonList(CustomerPaymentMethodPatchDto.builder()
                .id(customer.getPaymentMethods().getFirst().getId())
                .displayName("NEW NAME")
                .build()))
            .build();

        mockMvc.perform(patch(CustomerController.CUSTOMER_BASE_URL + "/{customerId}", UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerPatch)))
            .andExpect(status().isNotFound())
            .andExpect(openApi().isValid(OPENAPI_SPECIFICATION_URL));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        CustomerDto customer = buildTestCustomerDto();
        Customer savedCustomer = customerRepository.save(customerMapper.dtoToCustomer(customer));

        mockMvc.perform(delete(CustomerController.CUSTOMER_BASE_URL + "/{customerId}", savedCustomer.getId()))
            .andExpect(status().isNoContent())
            .andExpect(openApi().isValid(OPENAPI_SPECIFICATION_URL));

        assertThat(customerRepository.findById(savedCustomer.getId())).isEmpty();
    }

    @Test
    void testDeleteCustomerConflictsWithOrders() throws Exception {
        Customer customer = customerRepository.findAll().getFirst();

        mockMvc.perform(delete(CustomerController.CUSTOMER_BASE_URL + "/{customerId}", customer.getId()))
            .andExpect(status().isConflict())
            .andExpect(openApi().isValid(OPENAPI_SPECIFICATION_URL));
    }

    @Test
    void testDeleteNotFound() throws Exception {
        mockMvc.perform(delete(CustomerController.CUSTOMER_BASE_URL + "/{customerId}", UUID.randomUUID()))
            .andExpect(status().isNotFound())
            .andExpect(openApi().isValid(OPENAPI_SPECIFICATION_URL));
    }

    private CustomerDto buildTestCustomerDto() {
        return CustomerDto.builder()
            .name(NameDto.builder()
                .lastName("Doe")
                .firstName("John")
                .build())
            .phone("555-555-5555")
            .email("john@example.com")
            .shipToAddress(AddressDto.builder()
                .addressLine1("123 Main St")
                .city("Denver")
                .state("CO")
                .zip("80216")
                .build())
            .billToAddress(AddressDto.builder()
                .addressLine1("123 Main St")
                .city("Denver")
                .state("CO")
                .zip("80216")
                .build())
            .build();
    }
}
