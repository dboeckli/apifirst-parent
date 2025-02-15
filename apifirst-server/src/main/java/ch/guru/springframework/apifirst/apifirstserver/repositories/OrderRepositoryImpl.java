package ch.guru.springframework.apifirst.apifirstserver.repositories;

import ch.guru.springframework.apifirst.model.OrderDto;
import ch.guru.springframework.apifirst.model.OrderLineDto;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final Map<UUID, OrderDto> entityMap = new HashMap<>();

    @Override
    public <S extends OrderDto> S save(S entity) {
        OrderDto.OrderDtoBuilder builder = OrderDto.builder();

        builder.id(UUID.randomUUID())
            .orderStatus(entity.getOrderStatus())
            .shipmentInfo(entity.getShipmentInfo())
            .dateCreated(OffsetDateTime.now())
            .dateUpdated(OffsetDateTime.now());

        if (entity.getCustomer() != null) {
            builder.customer(entity.getCustomer());
        }

        if (entity.getOrderLines() != null){
            builder.orderLines(entity.getOrderLines().stream()
                .map(orderLine -> {
                    return OrderLineDto.builder()
                        .id(UUID.randomUUID())
                        .product(orderLine.getProduct()) //might cause NPE
                        .orderQuantity(orderLine.getOrderQuantity())
                        .shipQuantity(orderLine.getShipQuantity())
                        .dateCreated(OffsetDateTime.now())
                        .dateUpdated(OffsetDateTime.now())
                        .build();
                })
                .collect(Collectors.toList()));
        }

        OrderDto savedEntity = builder.build();
        entityMap.put(savedEntity.getId(), savedEntity);
        return (S) savedEntity;
    }

    @Override
    public <S extends OrderDto> Iterable<S> saveAll(Iterable<S> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
            .map(this::save)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDto> findById(UUID uuid) {
        return Optional.of(entityMap.get(uuid));
    }

    @Override
    public boolean existsById(UUID uuid) {
        return entityMap.get(uuid) != null;
    }

    @Override
    public Iterable<OrderDto> findAll() {
        return entityMap.values();
    }

    @Override
    public Iterable<OrderDto> findAllById(Iterable<UUID> uuids) {
        return StreamSupport.stream(uuids.spliterator(), false)
            .map(this::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return entityMap.size();
    }

    @Override
    public void deleteById(UUID uuid) {
        entityMap.remove(uuid);
    }

    @Override
    public void delete(OrderDto entity) {
        entityMap.remove(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {
        uuids.forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends OrderDto> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        entityMap.clear();
    }
}
