package ch.guru.springframework.apifirst.apifirstserver.jpa.mapper;

import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.PaymentMethod;
import ch.guru.springframework.apifirst.model.PaymentMethodDto;
import org.mapstruct.Mapper;

@Mapper
public interface PaymentMethodMapper {
    PaymentMethodDto paymentMethodToDto(PaymentMethod paymentMethod);
}
