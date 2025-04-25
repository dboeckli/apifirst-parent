package ch.guru.springframework.apifirst.apifirstserver.jpa.repositories;

import ch.guru.springframework.apifirst.apifirstserver.jpa.bootstrap.DataLoader;
import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Product;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@Import(DataLoader.class)
class ProductRepositoryIT {

    @Autowired
    ProductRepository productRepository;

    @Transactional
    @Test
    void testImagePersistence() {
        log.info("Testing image persistence...");
        Product product = productRepository.findAll().getFirst();

        assertAll("Product Image Persistence",
            () -> assertNotNull(product),
            () -> assertNotNull(product.getImages()),
            () -> assertFalse(product.getImages().isEmpty())
        );

    }
}
