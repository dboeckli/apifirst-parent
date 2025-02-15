package ch.guru.springframework.apifirst.apifirstserver.service;

import ch.guru.springframework.apifirst.model.Order;
import ch.guru.springframework.apifirst.model.OrderCreate;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<Order> listOrders();

    Order getOrderById(UUID orderId);

    Order saveNewOrder(OrderCreate orderCreate);
}
