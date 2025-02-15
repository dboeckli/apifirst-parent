package ch.guru.springframework.apifirst.apifirstserver.service;

import ch.guru.springframework.apifirst.apifirstserver.repositories.CustomerRepository;
import ch.guru.springframework.apifirst.model.Customer;
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
    public List<Customer> listCustomers() {
        return StreamSupport.stream(customerRepository.findAll().spliterator(), false).toList();
    }

    @Override
    public Customer getCustomerById(UUID customerId) {
        return customerRepository.findById(customerId).orElseThrow();
    }

    @Override
    public Customer saveNewCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
}
