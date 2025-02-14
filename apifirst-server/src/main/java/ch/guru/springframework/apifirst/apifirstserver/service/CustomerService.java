package ch.guru.springframework.apifirst.apifirstserver.service;

import ch.guru.springframework.apifirst.model.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> listCustomers();
}
