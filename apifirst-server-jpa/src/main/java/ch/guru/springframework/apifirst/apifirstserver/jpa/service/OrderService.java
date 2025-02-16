package ch.guru.springframework.apifirst.apifirstserver.jpa.service;

import ch.guru.springframework.apifirst.model.OrderCreateDto;
import ch.guru.springframework.apifirst.model.OrderDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<OrderDto> listOrders();

    OrderDto getOrderById(UUID orderId);

    OrderDto saveNewOrder(OrderCreateDto orderCreate);
}
