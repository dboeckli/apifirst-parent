package ch.guru.springframework.apifirst.apifirstserver.server.repositories;

import ch.guru.springframework.apifirst.model.OrderCustomerDto;
import ch.guru.springframework.apifirst.model.OrderDto;
import ch.guru.springframework.apifirst.model.OrderLineDto;
import ch.guru.springframework.apifirst.model.OrderProductDto;
import org.junit.jupiter.api.Test;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OrderRepositoryImplTest extends AbstractRepositoryContract<OrderDto> {

    private final OrderRepositoryImpl repository = new OrderRepositoryImpl();

    @Override
    protected CrudRepository<OrderDto, UUID> createRepository() {
        return new OrderRepositoryImpl();
    }

    @Override
    protected OrderDto newEntity() {
        return OrderDto.builder()
            .customer(OrderCustomerDto.builder().email("john.doe@example.com").phone("800-555-1212").build())
            .orderStatus(OrderDto.OrderStatusEnum.NEW)
            .shipmentInfo("shipment info")
            .orderLines(List.of(OrderLineDto.builder()
                .product(OrderProductDto.builder().description("Product").price("14.99").build())
                .orderQuantity(1)
                .shipQuantity(1)
                .build()))
            .build();
    }

    @Override
    protected UUID getId(OrderDto entity) {
        return entity.getId();
    }

    @Test
    void saveMapsCustomerAndOrderLines() {
        OrderDto saved = repository.save(newEntity());

        assertThat(saved.getCustomer().getEmail()).isEqualTo("john.doe@example.com");
        assertThat(saved.getOrderLines()).hasSize(1);
        assertThat(saved.getOrderLines().getFirst().getId()).isNotNull();
        assertThat(saved.getOrderLines().getFirst().getProduct().getDescription()).isEqualTo("Product");
    }

    @Test
    void saveWithDefaultOrderLines() {
        OrderDto order = OrderDto.builder()
            .customer(OrderCustomerDto.builder().email("john.doe@example.com").build())
            .orderStatus(OrderDto.OrderStatusEnum.NEW)
            .orderLines(List.of())
            .build();

        OrderDto saved = repository.save(order);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getOrderLines()).isEmpty();
    }

}
