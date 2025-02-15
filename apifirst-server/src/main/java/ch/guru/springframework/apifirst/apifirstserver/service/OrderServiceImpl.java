package ch.guru.springframework.apifirst.apifirstserver.service;

import ch.guru.springframework.apifirst.apifirstserver.repositories.CustomerRepository;
import ch.guru.springframework.apifirst.apifirstserver.repositories.OrderRepository;
import ch.guru.springframework.apifirst.apifirstserver.repositories.ProductRepository;
import ch.guru.springframework.apifirst.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    
    private final CustomerRepository customerRepository;
    
    private final ProductRepository productRepository;
    
    @Override
    public List<Order> listOrders() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false).toList();
    }

    @Override
    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }

    @Override
    public Order saveNewOrder(OrderCreate orderCreate) {
        Customer orderCustomer = customerRepository.findById(orderCreate.getCustomerId()).orElseThrow();

        Order.OrderBuilder orderBuilder = Order.builder()
            .customer(OrderCustomer.builder()
                .id(orderCustomer.getId())
                .name(orderCustomer.getName())
                .email(orderCustomer.getEmail())
                .billToAddress(orderCustomer.getBillToAddress())
                .shipToAddress(orderCustomer.getShipToAddress())
                .phone(orderCustomer.getPhone())
                .selectedPaymentMethod(orderCustomer.getPaymentMethods().stream()
                    .filter(paymentMethod -> paymentMethod.getId()
                        .equals(orderCreate.getSelectPaymentMethodId()))
                    .findFirst().orElseThrow())
                .build())
            .orderStatus(Order.OrderStatusEnum.NEW);

        List<OrderLine> orderLines = new ArrayList<>();

        orderCreate.getOrderLines()
            .forEach(orderLineCreate -> {
                Product product = productRepository.findById(orderLineCreate.getProductId()).orElseThrow();

                orderLines.add(OrderLine.builder()
                    .product(OrderProduct.builder()
                        .id(product.getId())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .build())
                    .orderQuantity(orderLineCreate.getOrderQuantity())
                    .shipQuantity(orderLineCreate.getOrderQuantity())
                    .build());
            });

        return orderRepository.save(orderBuilder.orderLines(orderLines).build());
    }
}
