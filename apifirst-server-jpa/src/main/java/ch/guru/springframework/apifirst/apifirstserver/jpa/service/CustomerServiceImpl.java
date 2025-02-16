package ch.guru.springframework.apifirst.apifirstserver.jpa.service;

import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.CustomerRepository;
import ch.guru.springframework.apifirst.model.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    
    @Override
    public List<CustomerDto> listCustomers() {
        //return StreamSupport.stream(customerRepository.findAll().spliterator(), false).toList();
        // TODO. Implement proper error handling and validation
        return Collections.emptyList();
    }

    @Override
    public CustomerDto getCustomerById(UUID customerId) {
        //return customerRepository.findById(customerId).orElseThrow();
        // TODO. Implement proper error handling and validation
        return null;
    }

    @Override
    public CustomerDto saveNewCustomer(CustomerDto customer) {
        // TODO. Implement proper error handling and validation
        //return customerRepository.save(customer);
        return null;
    }
}
