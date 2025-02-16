package ch.guru.springframework.apifirst.apifirstserver.jpa.repositories;

import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProductRepository extends CrudRepository<Product, UUID> {
}
