package ch.guru.springframework.apifirst.apifirstserver.controller;

import ch.guru.springframework.apifirst.apifirstserver.service.CustomerService;
import ch.guru.springframework.apifirst.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    
}
