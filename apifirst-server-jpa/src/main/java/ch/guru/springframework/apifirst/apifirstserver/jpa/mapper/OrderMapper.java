package ch.guru.springframework.apifirst.apifirstserver.jpa.mapper;

import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Order;
import ch.guru.springframework.apifirst.model.OrderCreateDto;
import ch.guru.springframework.apifirst.model.OrderDto;
import ch.guru.springframework.apifirst.model.OrderPatchDto;
import ch.guru.springframework.apifirst.model.OrderUpdateDto;
import org.mapstruct.*;

@Mapper
@DecoratedWith(OrderMapperDecorator.class)
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "shipmentInfo", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "selectedPaymentMethod", ignore = true)
    @Mapping(target = "orderLines", ignore = true)
    @Mapping(target = "dateUpdated", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void patchOrder(OrderPatchDto orderPatchDto, @MappingTarget Order target);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "shipmentInfo", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "selectedPaymentMethod", ignore = true)
    @Mapping(target = "orderLines", ignore = true)
    @Mapping(target = "dateUpdated", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    void updateOrder(OrderUpdateDto orderDto, @MappingTarget Order order);

    @Mapping(target = "selectPaymentMethodId", source = "selectedPaymentMethod.id")
    @Mapping(target = "orderLines", ignore = true)
    @Mapping(target = "customerId", source = "customer.id")
    OrderUpdateDto orderToUpdateDto(Order order);
    
    @Mapping(target = "shipmentInfo", ignore = true)
    @Mapping(target = "selectedPaymentMethod", ignore = true)
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
