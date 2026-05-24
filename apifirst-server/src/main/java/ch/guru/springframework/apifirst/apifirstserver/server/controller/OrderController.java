package ch.guru.springframework.apifirst.apifirstserver.server.controller;

import ch.guru.springframework.apifirst.apifirstserver.server.service.OrderService;
import ch.guru.springframework.apifirst.model.OrderCreateDto;
import ch.guru.springframework.apifirst.model.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(OrderController.ORDER_BASE_URL)
public class OrderController {

    public static final String ORDER_BASE_URL = "/v1/orders";

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> listOrders() {
        return ResponseEntity.ok(orderService.listOrders());
    }

    @PostMapping
    public ResponseEntity<Void> saveNewOrder(@RequestBody OrderCreateDto orderCreate) {
        OrderDto savedOrder = orderService.saveNewOrder(orderCreate);

        // we are returning the location in the header location field of the HTTP
        // response.
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{orderId}")
            .buildAndExpand(savedOrder.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("orderId") UUID orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

}
