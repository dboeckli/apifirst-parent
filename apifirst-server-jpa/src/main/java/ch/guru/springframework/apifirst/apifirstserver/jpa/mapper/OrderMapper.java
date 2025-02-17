package ch.guru.springframework.apifirst.apifirstserver.jpa.mapper;

import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Order;
import ch.guru.springframework.apifirst.model.OrderCreateDto;
import ch.guru.springframework.apifirst.model.OrderDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
@DecoratedWith(OrderMapperDecorator.class)
public interface OrderMapper {
    @Mapping(target = "shipmentInfo", ignore = true)
    //@Mapping(target = "selectedPaymentMethod", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateUpdated", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "customer", ignore = true)
    Order dtoToOrder(OrderCreateDto orderDto);

    @Mapping(target = "selectedPaymentMethod", ignore = true)
    @Mapping(target = "dateUpdated", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    Order dtoToOrder(OrderDto orderDto);

    OrderDto orderToDto(Order order);
}    
