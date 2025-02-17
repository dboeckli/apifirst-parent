package ch.guru.springframework.apifirst.apifirstserver.jpa.service;

import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Customer;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.CustomerRepository;
import ch.guru.springframework.apifirst.model.AddressDto;
import ch.guru.springframework.apifirst.model.CustomerDto;
import ch.guru.springframework.apifirst.model.NameDto;
import ch.guru.springframework.apifirst.model.PaymentMethodDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerServiceImplTest {

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerRepository customerRepository;
    
    @Test
    @Transactional
    void testSaveNewCustomer() {
        CustomerDto customerDto = createCustomerDTO();
        CustomerDto savedCustomer = customerService.saveNewCustomer(customerDto);
        Customer customer = customerRepository.findById(savedCustomer.getId()).orElseThrow();
        customerRepository.flush();
        
        assertAll(
            () -> assertNotNull(savedCustomer),
            () -> assertNotNull(savedCustomer.getId()),
            
            () -> assertNotNull(customer.getPaymentMethods()),
            () -> assertEquals(1, customer.getPaymentMethods().size()),
            () -> assertNotNull(customer.getPaymentMethods().getFirst().getId()),
            () -> assertEquals(savedCustomer.getId(), customer.getPaymentMethods().getFirst().getCustomer().getId()),
            
            () -> assertNotNull(customer.getShipToAddress().getId()),
            () -> assertNotNull(customer.getBillToAddress().getId()),
            
            () -> assertEquals(customerDto.getName().getFirstName(), customer.getName().getFirstName())
        );
    }

    @Test
    @Transactional
    void testListCustomers() {
        CustomerDto customerDtoFirst = createCustomerDTO();
        customerService.saveNewCustomer(customerDtoFirst);

        CustomerDto customerDtoSecond = createCustomerDTO();
        customerService.saveNewCustomer(customerDtoSecond);

        List<CustomerDto> customerDtoList = customerService.listCustomers();

        assertAll(
            () -> assertNotNull(customerDtoList),
            () -> assertEquals(2, customerDtoList.size())
        );
    }

    private CustomerDto createCustomerDTO() {
        return CustomerDto.builder()
            .name(NameDto.builder()
                .firstName("Fridolin")
                .lastName("Galaxus")
                .build())
            .billToAddress(AddressDto.builder()
                .addressLine1("1234 Main Street")
                .city("San Diego")
                .state("CA")
                .zip("92101")
                .build())
            .shipToAddress(AddressDto.builder()
                .addressLine1("1234 Main Street")
                .city("San Diego")
                .state("CA")
                .zip("92101")
                .build())
            .email("joe@example.com")
            .phone("555-555-5555")
            .paymentMethods(Collections.singletonList(PaymentMethodDto.builder()
                .displayName("My Card")
                .cardNumber(1234123412)
                .expiryMonth(12)
                .expiryYear(2020)
                .cvv(123).build()))
            .build();
    }

}
