package ch.guru.springframework.apifirst.apifirstserver.jpa.service;

import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.CustomerRepository;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.OrderRepository;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.ProductRepository;
import ch.guru.springframework.apifirst.model.OrderCreateDto;
import ch.guru.springframework.apifirst.model.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    
    private final CustomerRepository customerRepository;
    
    private final ProductRepository productRepository;
    
    @Override
    public List<OrderDto> listOrders() {
        // TODO. Implement proper error handling and validation
        //return StreamSupport.stream(orderRepository.findAll().spliterator(), false).toList();
        return Collections.emptyList();
    }

    @Override
    public OrderDto getOrderById(UUID orderId) {
        // TODO. Implement proper error handling and validation
        //return orderRepository.findById(orderId).orElseThrow();
        return null;
    }

    @Override
    public OrderDto saveNewOrder(OrderCreateDto orderCreate) {
        /*
        CustomerDto orderCustomer = customerRepository.findById(orderCreate.getCustomerId()).orElseThrow();

        OrderDto.OrderDtoBuilder orderBuilder = OrderDto.builder()
            .customer(OrderCustomerDto.builder()
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
            .orderStatus(OrderDto.OrderStatusEnum.NEW);

        List<OrderLineDto> orderLines = new ArrayList<>();

        orderCreate.getOrderLines()
            .forEach(orderLineCreate -> {
                ProductDto product = productRepository.findById(orderLineCreate.getProductId()).orElseThrow();

                orderLines.add(OrderLineDto.builder()
                    .product(OrderProductDto.builder()
                        .id(product.getId())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .build())
                    .orderQuantity(orderLineCreate.getOrderQuantity())
                    .shipQuantity(orderLineCreate.getOrderQuantity())
                    .build());
            });
        */
        // TODO. Implement proper error handling and validation
        //return orderRepository.save(orderBuilder.orderLines(orderLines).build());
        return null;
    }
}
