package ch.guru.springframework.apifirst.apifirstserver.service;

import ch.guru.springframework.apifirst.apifirstserver.repositories.CustomerRepository;
import ch.guru.springframework.apifirst.model.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    
    @Override
    public List<CustomerDto> listCustomers() {
        return StreamSupport.stream(customerRepository.findAll().spliterator(), false).toList();
    }

    @Override
    public CustomerDto getCustomerById(UUID customerId) {
        return customerRepository.findById(customerId).orElseThrow();
    }

    @Override
    public CustomerDto saveNewCustomer(CustomerDto customer) {
        return customerRepository.save(customer);
    }
}
