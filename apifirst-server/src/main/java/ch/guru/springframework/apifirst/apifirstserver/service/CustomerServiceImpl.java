package ch.guru.springframework.apifirst.apifirstserver.service;

import ch.guru.springframework.apifirst.apifirstserver.repositories.CustomerRepository;
import ch.guru.springframework.apifirst.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    
    @Override
    public List<Customer> listCustomers() {
        return StreamSupport.stream(customerRepository.findAll().spliterator(), false).toList();
    }
}
