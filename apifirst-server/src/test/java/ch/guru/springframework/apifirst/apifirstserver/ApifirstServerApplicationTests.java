package ch.guru.springframework.apifirst.apifirstserver;

import ch.guru.springframework.apifirst.apifirstserver.repositories.CustomerRepository;
import ch.guru.springframework.apifirst.apifirstserver.repositories.OrderRepository;
import ch.guru.springframework.apifirst.apifirstserver.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Test
    void contextLoads() {
        assertNotNull(applicationContext, "Application context should not be null");
        log.info("Testing Spring 6 Application {}", applicationContext.getApplicationName());
    }

    @Test
    void testDataLoad() {
        assertThat(customerRepository.count()).isPositive();
        assertThat(productRepository.count()).isPositive();
        assertThat(orderRepository.count()).isPositive();
    }

}
