package ch.guru.springframework.apifirst.apifirstserver.server.controller;

import ch.guru.springframework.apifirst.apifirstserver.server.repositories.CustomerRepository;
import ch.guru.springframework.apifirst.apifirstserver.server.repositories.OrderRepository;
import ch.guru.springframework.apifirst.apifirstserver.server.repositories.ProductRepository;
import ch.guru.springframework.apifirst.model.*;
import jakarta.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.Collections;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class OrderControllerTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext wac;

    public MockMvc mockMvc;

    @Autowired
    Filter validationFilter;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .addFilter(validationFilter)
            .build();
    }

    @Test
    @Order(1)
    void testListOrders() throws Exception {
        mockMvc.perform(get(OrderController.ORDER_BASE_URL)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", greaterThan(0)));
    }

    @Test
    @Order(2)
    void testGetOrderById() throws Exception {
        OrderDto testOrder = orderRepository.findAll().iterator().next();

        mockMvc.perform(get(OrderController.ORDER_BASE_URL + "/{orderId}", testOrder.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(testOrder.getId().toString()));
    }

    @Test
    @Order(3)
    void testCreateOrder() throws Exception {
        CustomerDto testCustomer = customerRepository.findAll().iterator().next();
        ProductDto testProduct = productRepository.findAll().iterator().next();

        OrderCreateDto orderCreate = OrderCreateDto.builder()
            .customerId(testCustomer.getId())
            .selectPaymentMethodId(testCustomer.getPaymentMethods().getFirst().getId())
            .orderLines(Collections.singletonList(OrderLineCreateDto.builder()
                .productId(testProduct.getId())
                .orderQuantity(1)
                .build()))
            .build();

        log.info("created order: {}", objectMapper.writeValueAsString(orderCreate));

        MvcResult result = mockMvc.perform(post(OrderController.ORDER_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderCreate)))
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
            .andExpect(status().isOk());
    }

}
