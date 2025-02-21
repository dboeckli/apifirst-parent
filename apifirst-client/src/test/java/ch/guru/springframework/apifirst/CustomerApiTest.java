package ch.guru.springframework.apifirst;

import ch.guru.springframework.apifirst.client.CustomerApi;
import ch.guru.springframework.apifirst.model.CustomerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerApiTest {

    CustomerApi customerApiJpa;

    CustomerApi customerApiServer;
    
    @BeforeEach
    void setup() {
        ApiClient apiClientJpa = new ApiClient(new RestTemplate());

        apiClientJpa.setBasePath("http://localhost:" + 8081);
        customerApiJpa = new CustomerApi(apiClientJpa);

        apiClientJpa.setBasePath("http://localhost:" + 8082);
        customerApiServer = new CustomerApi(apiClientJpa);
    }  
    
    @Test
    void testListCustomersInJpaModule() {
        List<CustomerDto> customers = customerApiJpa.listCustomers();
        assertThat(customers).isNotEmpty();
    }

    @Test
    void testListCustomersInServerModule() {
        List<CustomerDto> customers = customerApiJpa.listCustomers();
        assertThat(customers).isNotEmpty();
    }
}
