package ch.guru.springframework.apifirst.apifirstserver.jpa.service;

import ch.guru.springframework.apifirst.apifirstserver.jpa.mapper.CustomerMapper;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.CustomerRepository;
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
    
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDto saveNewCustomer(CustomerDto customer) {
        return customerMapper.customerToDto(customerRepository.save(customerMapper.dtoToCustomer(customer)));
    }

    @Override
    public List<CustomerDto> listCustomers() {
        return StreamSupport.stream(customerRepository.findAll().spliterator(), false)
            .map(customerMapper::customerToDto)
            .toList();
    }

    @Override
    public CustomerDto getCustomerById(UUID customerId) {
        return customerMapper.customerToDto(customerRepository.findById(customerId).orElseThrow());
    }
    
}
