package ch.guru.springframework.apifirst.apifirstserver.jpa.repositories;

import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Product;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Transactional
    @Test
    void testImagePersistence() {
        Product product = productRepository.findAll().getFirst();

        assertAll("Product Image Persistence",
            () -> assertNotNull(product),
            () -> assertNotNull(product.getImages()),
            () -> assertFalse(product.getImages().isEmpty())
        );

    }
}
