package ch.guru.springframework.apifirst.apifirstserver.jpa.service;

import ch.guru.springframework.apifirst.model.OrderCreateDto;
import ch.guru.springframework.apifirst.model.OrderDto;
import ch.guru.springframework.apifirst.model.OrderPatchDto;
import ch.guru.springframework.apifirst.model.OrderUpdateDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<OrderDto> listOrders();

    OrderDto getOrderById(UUID orderId);

    OrderDto saveNewOrder(OrderCreateDto orderCreate);

    OrderDto updateOrder(UUID orderId, OrderUpdateDto orderUpdate);

    OrderDto patchOrder(UUID orderId, OrderPatchDto orderPatch);

    void deleteOrder(UUID orderId);
}
