package ch.guru.springframework.apifirst.apifirstserver.controller;

import ch.guru.springframework.apifirst.apifirstserver.service.CustomerService;
import ch.guru.springframework.apifirst.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(CustomerController.CUSTOMER_BASE_URL)
public class CustomerController {
    
    public static final String CUSTOMER_BASE_URL = "/v1/customers";
    
    private final CustomerService customerService;
    
    @GetMapping
    public ResponseEntity<List<Customer>> listCustomers() {
        return ResponseEntity.ok(customerService.listCustomers());
    }

    @PostMapping
    public ResponseEntity<Void> saveNewCustomer(@RequestBody Customer customer){
        Customer savedCustomer = customerService.saveNewCustomer(customer);

        // we are returning the location in the header location field of the HTTP response.
        UriComponents uriComponents = UriComponentsBuilder.fromPath(CUSTOMER_BASE_URL + "/{customer_id}")
            .buildAndExpand(savedCustomer.getId());

        return ResponseEntity.created(URI.create(uriComponents.getPath())).build();
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("customerId") UUID customerId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }
    
}
