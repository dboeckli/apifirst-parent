package ch.guru.springframework.apifirst.apifirstserver.service;

import ch.guru.springframework.apifirst.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    List<Customer> listCustomers();

    Customer getCustomerById(UUID customerId);
}
