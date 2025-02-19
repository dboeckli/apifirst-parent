package ch.guru.springframework.apifirst;

import ch.guru.springframework.apifirst.client.CustomerApi;
import ch.guru.springframework.apifirst.model.CustomerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerApiTest {

    @LocalServerPort
    private int port;

    ApiClient apiClient;
    
    CustomerApi customerApi;
    
    @BeforeEach
    void setup() {
        apiClient = new ApiClient(new RestTemplate());
        apiClient.setBasePath("http://localhost:" + 8081);
        customerApi = new CustomerApi(apiClient);
    }  
    
    @Test
    void testListCustomers() {
        ResponseEntity<List<CustomerDto>> response = customerApi.listCustomersWithHttpInfo();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
