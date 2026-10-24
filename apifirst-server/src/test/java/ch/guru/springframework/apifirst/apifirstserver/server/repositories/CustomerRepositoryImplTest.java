package ch.guru.springframework.apifirst.apifirstserver.server.repositories;

import ch.guru.springframework.apifirst.model.AddressDto;
import ch.guru.springframework.apifirst.model.CustomerDto;
import ch.guru.springframework.apifirst.model.NameDto;
import ch.guru.springframework.apifirst.model.PaymentMethodDto;
import org.junit.jupiter.api.Test;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerRepositoryImplTest extends AbstractRepositoryContract<CustomerDto> {

    private final CustomerRepositoryImpl repository = new CustomerRepositoryImpl();

    @Override
    protected CrudRepository<CustomerDto, UUID> createRepository() {
        return new CustomerRepositoryImpl();
    }

    @Override
    protected CustomerDto newEntity() {
        return CustomerDto.builder()
            .name(NameDto.builder().firstName("John").lastName("Doe").build())
            .email("john.doe@example.com")
            .phone("800-555-1212")
            .billToAddress(address())
            .shipToAddress(address())
            .paymentMethods(List.of(PaymentMethodDto.builder()
                .displayName("My Card")
                .cardNumber(12341234)
                .expiryMonth(12)
                .expiryYear(26)
                .cvv(123)
                .build()))
            .build();
    }

    @Override
    protected UUID getId(CustomerDto entity) {
        return entity.getId();
    }

    @Test
    void saveMapsNestedAddressesAndPaymentMethods() {
        CustomerDto saved = repository.save(newEntity());

        assertThat(saved.getBillToAddress().getId()).isNotNull();
        assertThat(saved.getShipToAddress().getId()).isNotNull();
        assertThat(saved.getBillToAddress().getCity()).isEqualTo("Some City");
        assertThat(saved.getPaymentMethods()).hasSize(1);
        assertThat(saved.getPaymentMethods().getFirst().getId()).isNotNull();
        assertThat(saved.getPaymentMethods().getFirst().getDisplayName()).isEqualTo("My Card");
    }

    private AddressDto address() {
        return AddressDto.builder()
            .addressLine1("1234 W Some Street")
            .city("Some City")
            .state("FL")
            .zip("33701")
            .build();
    }

}
