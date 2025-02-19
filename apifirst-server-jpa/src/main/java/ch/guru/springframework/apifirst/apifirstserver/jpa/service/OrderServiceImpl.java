package ch.guru.springframework.apifirst.apifirstserver.jpa.service;

import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Order;
import ch.guru.springframework.apifirst.apifirstserver.jpa.mapper.OrderMapper;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.OrderRepository;
import ch.guru.springframework.apifirst.model.OrderCreateDto;
import ch.guru.springframework.apifirst.model.OrderDto;
import ch.guru.springframework.apifirst.model.OrderPatchDto;
import ch.guru.springframework.apifirst.model.OrderUpdateDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    
    @Override
    public List<OrderDto> listOrders() {
        return orderRepository.findAll().stream()
            .map(orderMapper::orderToDto)
            .toList();
    }

    @Override
    public OrderDto getOrderById(UUID orderId) {
        return orderMapper.orderToDto(orderRepository.findById(orderId).orElseThrow());
    }

    @Override
    public OrderDto saveNewOrder(OrderCreateDto orderCreate) {
        Order savedOrder = orderRepository.saveAndFlush(orderMapper.dtoToOrder(orderCreate));
        return orderMapper.orderToDto(savedOrder);
    }

    @Transactional
    @Override
    public OrderDto updateOrder(UUID orderId, OrderUpdateDto orderUpdateDto) {
        Order existingOrder = orderRepository.findById(orderId).orElseThrow();
        orderMapper.updateOrder(orderUpdateDto, existingOrder);

        Order savedOrder = orderRepository.saveAndFlush(existingOrder);

        return orderMapper.orderToDto(savedOrder);
    }

    @Transactional
    @Override
    public OrderDto patchOrder(UUID orderId, OrderPatchDto orderPatchDto) {
        Order existingOrder = orderRepository.findById(orderId).orElseThrow();
        orderMapper.patchOrder(orderPatchDto, existingOrder);

        Order savedOrder = orderRepository.saveAndFlush(existingOrder);

        return orderMapper.orderToDto(savedOrder);
    }

    @Transactional
    @Override
    public void deleteOrder(UUID orderId) {
        orderRepository.findById(orderId).ifPresentOrElse(order -> orderRepository.deleteById(orderId), () -> {
            throw new NotFoundException("Order not found: " + orderId);
        });
    }
}
