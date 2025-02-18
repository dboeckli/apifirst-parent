package ch.guru.springframework.apifirst.apifirstserver.jpa.service;

import ch.guru.springframework.apifirst.model.CustomerDto;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    List<CustomerDto> listCustomers();

    CustomerDto getCustomerById(UUID customerId);

    CustomerDto saveNewCustomer(CustomerDto customer);

    CustomerDto updateCustomer(UUID customerId, CustomerDto customer);
}
