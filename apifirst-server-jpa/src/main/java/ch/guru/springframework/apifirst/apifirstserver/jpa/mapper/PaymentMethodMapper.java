package ch.guru.springframework.apifirst.apifirstserver.jpa.mapper;

import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.PaymentMethod;
import ch.guru.springframework.apifirst.model.CustomerPaymentMethodPatchDto;
import ch.guru.springframework.apifirst.model.PaymentMethodDto;
import org.mapstruct.*;

@Mapper
public interface PaymentMethodMapper {
    PaymentMethodDto paymentMethodToDto(PaymentMethod paymentMethod);

    @Mapping(target = "dateUpdated", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void updatePaymentMethod(CustomerPaymentMethodPatchDto customerPaymentMethodPatchDto,
                             @MappingTarget PaymentMethod paymentMethod);
}
