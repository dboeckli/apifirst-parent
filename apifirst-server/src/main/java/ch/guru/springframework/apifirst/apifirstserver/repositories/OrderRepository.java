package ch.guru.springframework.apifirst.apifirstserver.repositories;

import ch.guru.springframework.apifirst.model.Order;
import ch.guru.springframework.apifirst.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OrderRepository extends CrudRepository<Order, UUID> {
}
