package ch.guru.springframework.apifirst.apifirstserver.jpa.controller;

import ch.guru.springframework.apifirst.apifirstserver.jpa.service.CustomerService;
import ch.guru.springframework.apifirst.model.CustomerDto;
import ch.guru.springframework.apifirst.model.CustomerPatchDto;
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
    public ResponseEntity<List<CustomerDto>> listCustomers() {
        return ResponseEntity.ok(customerService.listCustomers());
    }

    @PostMapping
    public ResponseEntity<Void> saveNewCustomer(@RequestBody CustomerDto customer){
        CustomerDto savedCustomer = customerService.saveNewCustomer(customer);

        // we are returning the location in the header location field of the HTTP response.
        UriComponents uriComponents = UriComponentsBuilder.fromPath(CUSTOMER_BASE_URL + "/{customer_id}")
            .buildAndExpand(savedCustomer.getId());

        return ResponseEntity.created(URI.create(uriComponents.getPath())).build();
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable("customerId") UUID customerId, 
                                                     @RequestBody CustomerDto updateCustomer) {
        CustomerDto updatedCustomer = customerService.updateCustomer(customerId, updateCustomer);
        return ResponseEntity.ok(updatedCustomer);
    }

    @PatchMapping("/{customerId}")
    public ResponseEntity<CustomerDto> patchCustomer(@PathVariable("customerId") UUID customerId,
                                                     @RequestBody CustomerPatchDto patchCustomer) {
        CustomerDto patchedCustomer = customerService.patchCustomer(customerId, patchCustomer);
        return ResponseEntity.ok(patchedCustomer);
    }
    
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("customerId") UUID customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable("customerId") UUID customerId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }
    
}
