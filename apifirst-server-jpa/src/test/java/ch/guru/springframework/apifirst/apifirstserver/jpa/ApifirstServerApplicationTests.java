package ch.guru.springframework.apifirst.apifirstserver.jpa;

import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.CategoryRepository;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.CustomerRepository;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.OrderRepository;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
@Slf4j
class ApifirstServerApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    CustomerRepository customerRepository;
    
    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;
    
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext, "Application context should not be null");
        log.info("Testing Spring 6 Application {}", applicationContext.getApplicationName());
    }

    @Test
    void testDataLoad() {
        assertAll("Data should be empty at start",
            () -> assertEquals(2, customerRepository.count()),
            () -> assertEquals(2, productRepository.count()),
            () -> assertEquals(2, orderRepository.count()),
            () -> assertEquals(3, categoryRepository.count())
        );
    }

}
