package ch.guru.springframework.apifirst.apifirstserver.jpa.mapper;

import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.*;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.CustomerRepository;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.ProductRepository;
import ch.guru.springframework.apifirst.model.OrderCreateDto;
import ch.guru.springframework.apifirst.model.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

public abstract class OrderMapperDecorator implements OrderMapper {
    
    @Autowired
    @Qualifier("delegate")
    private OrderMapper delegate;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentMethodMapper paymentMethodMapper;

    @Override
    public Order dtoToOrder(OrderCreateDto orderCreate) {
        Customer orderCustomer = customerRepository.findById(orderCreate.getCustomerId()).orElseThrow();

        PaymentMethod selectedPaymentMethod = orderCustomer.getPaymentMethods().stream()
            .filter(pm -> pm.getId().equals(orderCreate.getSelectPaymentMethodId()))
            .findFirst()
            .orElseThrow();

        Order.OrderBuilder builder = Order.builder()
            .customer(orderCustomer)
            .selectedPaymentMethod(selectedPaymentMethod)
            .orderStatus(OrderStatusEnum.NEW);

        List<OrderLine> orderLines = new ArrayList<>();

        orderCreate.getOrderLines()
            .forEach(orderLineCreate -> {
                Product product = productRepository.findById(orderLineCreate.getProductId()).orElseThrow();

                orderLines.add(OrderLine.builder()
                    .product(product)
                    .orderQuantity(orderLineCreate.getOrderQuantity())
                    .build());
            });

        return builder.orderLines(orderLines).build();
    }

    @Override
    public Order dtoToOrder(OrderDto orderDto) {
        return delegate.dtoToOrder(orderDto);
    }

    @Override
    public OrderDto orderToDto(Order order) {
        OrderDto orderDto = delegate.orderToDto(order);
        orderDto.getCustomer().selectedPaymentMethod(paymentMethodMapper.paymentMethodToDto(order.getSelectedPaymentMethod()));
        return orderDto;
    }
}
