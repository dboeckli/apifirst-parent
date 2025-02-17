package ch.guru.springframework.apifirst.apifirstserver.server.repositories;

import ch.guru.springframework.apifirst.model.OrderDto;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OrderRepository extends CrudRepository<OrderDto, UUID> {
}
