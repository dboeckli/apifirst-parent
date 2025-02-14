package ch.guru.springframework.apifirst.apifirstserver.repositories;

import ch.guru.springframework.apifirst.model.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CustomerRepository extends CrudRepository<Customer, UUID> {
}
