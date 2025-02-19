package ch.guru.springframework.apifirst.apifirstserver.jpa.service;

import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Customer;
import ch.guru.springframework.apifirst.apifirstserver.jpa.mapper.CustomerMapper;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.CustomerRepository;
import ch.guru.springframework.apifirst.model.CustomerDto;
import ch.guru.springframework.apifirst.model.CustomerPatchDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    
    private final CustomerMapper customerMapper;

    @Transactional
    @Override
    public CustomerDto saveNewCustomer(CustomerDto customer) {
        return customerMapper.customerToDto(customerRepository.save(customerMapper.dtoToCustomer(customer)));
    }

    @Transactional
    @Override
    public CustomerDto updateCustomer(UUID customerId, CustomerDto customer) {
        Customer existingCustomer = customerRepository.findById(customerId).orElseThrow();
        customerMapper.updateCustomer(customer, existingCustomer);

        return customerMapper.customerToDto(customerRepository.save(existingCustomer));
    }

    @Override
    @Transactional
    public CustomerDto patchCustomer(UUID customerId, CustomerPatchDto patchCustomer) {
        Customer existingCustomer = customerRepository.findById(customerId).orElseThrow();
        customerMapper.patchCustomer(patchCustomer, existingCustomer);

        return customerMapper.customerToDto(customerRepository.save(existingCustomer));
    }

    @Override
    @Transactional
    public void deleteCustomer(UUID customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<CustomerDto> listCustomers() {
        return customerRepository.findAll().stream()
            .map(customerMapper::customerToDto)
            .toList();
    }

    @Override
    public CustomerDto getCustomerById(UUID customerId) {
        return customerMapper.customerToDto(customerRepository.findById(customerId).orElseThrow());
    }
    
}
