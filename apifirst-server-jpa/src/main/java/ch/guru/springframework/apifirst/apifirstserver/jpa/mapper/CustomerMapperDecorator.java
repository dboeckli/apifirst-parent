package ch.guru.springframework.apifirst.apifirstserver.jpa.mapper;

import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Customer;
import ch.guru.springframework.apifirst.model.CustomerDto;
import ch.guru.springframework.apifirst.model.CustomerPatchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class CustomerMapperDecorator implements CustomerMapper {

    @Autowired
    @Qualifier("delegate")
    private CustomerMapper delegate;

    @Autowired
    private PaymentMethodMapper paymentMethodMapper;

    @Override
    public CustomerPatchDto customerToPatchDto(Customer customer) {
        return delegate.customerToPatchDto(customer);
    }

    @Override
    public void patchCustomer(CustomerPatchDto customerPatchDto, Customer target) {
        delegate.patchCustomer(customerPatchDto, target);

        if (customerPatchDto.getPaymentMethods() != null) {
            customerPatchDto.getPaymentMethods().forEach(paymentMethodPatchDto -> target.getPaymentMethods().forEach(paymentMethod -> {
                if (paymentMethod.getId().equals(paymentMethodPatchDto.getId())) {
                    paymentMethodMapper.updatePaymentMethod(paymentMethodPatchDto, paymentMethod);
                }
            }));
        }
    }

    @Override
    public CustomerDto customerToDto(Customer customer) {
        return delegate.customerToDto(customer);
    }

    @Override
    public Customer dtoToCustomer(CustomerDto customerDto) {
        return delegate.dtoToCustomer(customerDto);
    }

    @Override
    public void updateCustomer(CustomerDto customerDto, Customer customer) {
        delegate.updateCustomer(customerDto, customer);
    }
}
