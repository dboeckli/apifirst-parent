package ch.guru.springframework.apifirst.apifirstserver.jpa.repositories;

import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Customer;
import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    
    List<Order> findAllByCustomer(Customer customer);
    
}
