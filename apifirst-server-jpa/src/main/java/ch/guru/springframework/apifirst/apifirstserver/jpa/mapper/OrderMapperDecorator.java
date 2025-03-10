package ch.guru.springframework.apifirst.apifirstserver.jpa.mapper;

import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.*;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.CustomerRepository;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.ProductRepository;
import ch.guru.springframework.apifirst.model.*;
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
    public void patchOrder(OrderPatchDto orderPatchDto, Order target) {
        delegate.patchOrder(orderPatchDto, target);

        if (orderPatchDto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(orderPatchDto.getCustomerId()).orElseThrow();
            target.setCustomer(customer);
        }
        if (orderPatchDto.getSelectPaymentMethodId() != null) {
            PaymentMethod selectedPaymentMethod = target.getCustomer().getPaymentMethods().stream()
                    .filter(pm -> pm.getId().equals(orderPatchDto.getSelectPaymentMethodId()))
                    .findFirst()
                    .orElseThrow();
            target.setSelectedPaymentMethod(selectedPaymentMethod);
        }
        if (orderPatchDto.getOrderLines() != null && !orderPatchDto.getOrderLines().isEmpty()) {
            orderPatchDto.getOrderLines().forEach(orderLinePatchDto -> {
                OrderLine existingOrderLine = target.getOrderLines().stream()
                        .filter(ol -> ol.getId().equals(orderLinePatchDto.getId()))
                        .findFirst()
                        .orElseThrow();

                if (orderLinePatchDto.getProductId() != null) {
                    Product product = productRepository.findById(orderLinePatchDto.getProductId()).orElseThrow();
                    existingOrderLine.setProduct(product);
                }

                if (orderLinePatchDto.getOrderQuantity() != null) {
                    existingOrderLine.setOrderQuantity(orderLinePatchDto.getOrderQuantity());
                }
            });
        }
    }

    @Override
    public void updateOrder(OrderUpdateDto orderDto, Order order) {
        delegate.updateOrder(orderDto, order);

        Customer orderCustomer = customerRepository.findById(orderDto.getCustomerId()).orElseThrow();

        order.setCustomer(orderCustomer);

        PaymentMethod selectedPaymentMethod = order.getCustomer().getPaymentMethods().stream()
                .filter(pm -> pm.getId().equals(orderDto.getSelectPaymentMethodId()))
                .findFirst()
                .orElseThrow();

        order.setSelectedPaymentMethod(selectedPaymentMethod);

        if (orderDto.getOrderLines() != null && !orderDto.getOrderLines().isEmpty()) {
            orderDto.getOrderLines().forEach(orderLineDto -> {
                OrderLine existingOrderLine = order.getOrderLines().stream()
                        .filter(ol -> ol.getId().equals(orderLineDto.getId()))
                        .findFirst().orElseThrow();

                Product product = productRepository.findById(orderLineDto.getProductId()).orElseThrow();

                existingOrderLine.setProduct(product);
                existingOrderLine.setOrderQuantity(orderLineDto.getOrderQuantity());
            });
        }
    }

    @Override
    public OrderUpdateDto orderToUpdateDto(Order order) {
        OrderUpdateDto orderUpdateDto = delegate.orderToUpdateDto(order);

        List<OrderLineUpdateDto> updatedOrderLines = new ArrayList<>();

        order.getOrderLines().forEach(orderLine -> {
            OrderLineUpdateDto orderLineDto = OrderLineUpdateDto.builder()
                    .id(orderLine.getId())
                    .orderQuantity(orderLine.getOrderQuantity())
                    .productId(orderLine.getProduct().getId())
                    .build();
            updatedOrderLines.add(orderLineDto);
        });
        orderUpdateDto.setOrderLines(updatedOrderLines);
        return orderUpdateDto;
    }

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

        Order order = builder.orderLines(orderLines).build();
        orderLines.forEach(orderLine -> orderLine.setOrder(order));
        return order;
    }

    @Override
    public Order dtoToOrder(OrderDto orderDto) {
        return delegate.dtoToOrder(orderDto);
    }

    @Override
    public OrderDto orderToDto(Order order) {
        OrderDto orderDto = delegate.orderToDto(order);
        orderDto.getCustomer()
                .selectedPaymentMethod(paymentMethodMapper.paymentMethodToDto(order.getSelectedPaymentMethod()));

        return orderDto;
    }
}
