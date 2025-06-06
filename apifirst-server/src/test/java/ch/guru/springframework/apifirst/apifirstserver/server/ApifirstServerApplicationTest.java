package ch.guru.springframework.apifirst.apifirstserver.server;

import ch.guru.springframework.apifirst.apifirstserver.server.repositories.CustomerRepository;
import ch.guru.springframework.apifirst.apifirstserver.server.repositories.OrderRepository;
import ch.guru.springframework.apifirst.apifirstserver.server.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
@Slf4j
class ApifirstServerApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    CustomerRepository customerRepository;
    
    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext);
        log.info("Testing Spring 6 Application {}", applicationContext.getApplicationName());
    }

    @Test
    void testDataLoad() {
        assertThat(customerRepository.count()).isPositive();
        assertThat(productRepository.count()).isPositive();
        assertThat(orderRepository.count()).isPositive();
    }

}
