package ch.guru.springframework.apifirst.apifirstserver.jpa.controller;

import ch.guru.springframework.apifirst.apifirstserver.jpa.bootstrap.DataLoader;
import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Customer;
import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Order;
import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Product;
import ch.guru.springframework.apifirst.apifirstserver.jpa.mapper.OrderMapper;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.CustomerRepository;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.OrderRepository;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.ProductRepository;
import ch.guru.springframework.apifirst.model.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(DataLoader.class)
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
    OrderMapper orderMapper;

    @Autowired
    WebApplicationContext wac;

    public MockMvc mockMvc;

    @Autowired
    Filter validationFilter;

    @BeforeEach
    void setUp() {
        objectMapper.configure(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION, true);
        
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .addFilter(validationFilter)
            .build();
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void testListOrders() throws Exception {
        mockMvc.perform(get(OrderController.ORDER_BASE_URL)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", greaterThan(0)));
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void testGetOrderById() throws Exception {
        ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Order testOrder = orderRepository.findAll().getFirst();

        mockMvc.perform(get(OrderController.ORDER_BASE_URL + "/{orderId}", testOrder.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(testOrder.getId().toString()));
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void testGetOrderByIdNotFound() throws Exception {
        mockMvc.perform(get(OrderController.ORDER_BASE_URL + "/{orderId}", UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    @Transactional
    void testCreateOrder() throws Exception {
        Customer testCustomer = customerRepository.findAll().getFirst();
        Product testProduct = productRepository.findAll().getFirst();

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
        assertNotNull(locationHeader);

        // Extract the URI from the location header
        URI locationUri = new URI(locationHeader);
        String path = locationUri.getPath();
        log.info("Extracted path: {}", path);

        // Perform GET request using the extracted path
        mockMvc.perform(get(path)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @org.junit.jupiter.api.Order(4)
    void testUpdateOrder() throws Exception {
        Order order = orderRepository.findAll().getFirst();

        order.getOrderLines().getFirst().setOrderQuantity(222);

        OrderUpdateDto orderUpdate = orderMapper.orderToUpdateDto(order);

        mockMvc.perform(put(OrderController.ORDER_BASE_URL + "/{orderId}", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderUpdate))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(order.getId().toString())))
            .andExpect(jsonPath("$.orderLines[0].orderQuantity", equalTo(222)));
    }

    @Transactional
    @Test
    @org.junit.jupiter.api.Order(4)
    void testUpdateOrderNotFound() throws Exception {
        Order order = orderRepository.findAll().getFirst();

        order.getOrderLines().getFirst().setOrderQuantity(222);

        OrderUpdateDto orderUpdate = orderMapper.orderToUpdateDto(order);


        mockMvc.perform(put(OrderController.ORDER_BASE_URL + "/{orderId}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderUpdate))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Transactional
    @org.junit.jupiter.api.Order(4)
    @Test
    void testPatchOrder() throws Exception {

        Order order = orderRepository.findAll().getFirst();

        OrderPatchDto orderPatch = OrderPatchDto.builder()
            .orderLines(Collections.singletonList(OrderLinePatchDto.builder()
                .id(order.getOrderLines().getFirst().getId())
                .orderQuantity(333)
                .build()))
            .build();

        mockMvc.perform(patch(OrderController.ORDER_BASE_URL + "/{orderId}", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderPatch))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(order.getId().toString())))
            .andExpect(jsonPath("$.orderLines[0].orderQuantity", equalTo(333)));
    }

    @Transactional
    @Test
    void testDeleteOrder() throws Exception {
        OrderCreateDto dto = createNewOrderDto();
        Order savedOrder = orderRepository.save(orderMapper.dtoToOrder(dto));

        mockMvc.perform(delete(OrderController.ORDER_BASE_URL + "/{orderId}", savedOrder.getId()))
            .andExpect(status().isNoContent());

        assertThat(orderRepository.findById(savedOrder.getId())).isEmpty();
    }

    @Test
    void testDeleteNotFound() throws Exception {
        mockMvc.perform(delete(OrderController.ORDER_BASE_URL + "/{orderId}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    private OrderCreateDto createNewOrderDto() {
        Customer testCustomer = customerRepository.findAll().getFirst();
        Product testProduct = productRepository.findAll().getFirst();
        return OrderCreateDto.builder()
            .customerId(testCustomer.getId())
            .selectPaymentMethodId(testCustomer.getPaymentMethods().getFirst().getId())
            .orderLines(Collections.singletonList(OrderLineCreateDto.builder()
                .productId(testProduct.getId())
                .orderQuantity(1)
                .build()))
            .build();
    }

}
