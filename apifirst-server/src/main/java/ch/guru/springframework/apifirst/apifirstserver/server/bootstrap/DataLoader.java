package ch.guru.springframework.apifirst.apifirstserver.server.bootstrap;

import ch.guru.springframework.apifirst.apifirstserver.server.repositories.CategoryRepository;
import ch.guru.springframework.apifirst.apifirstserver.server.repositories.CustomerRepository;
import ch.guru.springframework.apifirst.apifirstserver.server.repositories.OrderRepository;
import ch.guru.springframework.apifirst.apifirstserver.server.repositories.ProductRepository;
import ch.guru.springframework.apifirst.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        // Create customers
        AddressDto address1 = AddressDto.builder()
            .addressLine1("1234 W Some Street")
            .city("Some City")
            .state("FL")
            .zip("33701")
            .build();

        CustomerDto customer1 = CustomerDto.builder()
            .name(NameDto.builder()
                .firstName("John")
                .lastName("Thompson")
                .build())
            .billToAddress(address1)
            .shipToAddress(address1)
            .email("john@springframework.guru")
            .phone("800-555-1212")
            .paymentMethods(List.of(PaymentMethodDto.builder()
                .displayName("My Card")
                .cardNumber(12341234)
                .expiryMonth(12)
                .expiryYear(26)
                .cvv(123)
                .build()))
            .build();

        AddressDto address2 = AddressDto.builder()
            .addressLine1("1234 W Some Street")
            .city("Some City")
            .state("FL")
            .zip("33701")
            .build();

        CustomerDto customer2 = CustomerDto.builder()
            .name(NameDto.builder()
                .firstName("Jim")
                .lastName("Smith")
                .build())
            .billToAddress(address2)
            .shipToAddress(address2)
            .email("jim@springframework.guru")
            .phone("800-555-1212")
            .paymentMethods(List.of(PaymentMethodDto.builder()
                .displayName("My Other Card")
                .cardNumber(1234888)
                .expiryMonth(12)
                .expiryYear(26)
                .cvv(456)
                .build()))
            .build();

        CustomerDto savedCustomer1 = customerRepository.save(customer1);
        CustomerDto savedCustomer2 = customerRepository.save(customer2);
        log.info("Customers loaded: {}", customerRepository.count());

        // Create Categories
        CategoryDto category1 = CategoryDto.builder()
            .category("Category 1")
            .description("Category 1 Description")
            .categoryCode("Electronics")
            .build();

        CategoryDto category2 = CategoryDto.builder()
            .category("Category 2")
            .description("Category 2 Description")
            .categoryCode("Gifts")
            .build();

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        
        // Create Products
        ProductDto product1 = ProductDto.builder()
            .description("Product 1")
            .categories(List.of(CategoryDto.builder()
                .category("Category 1")
                .description("Category 1 Description")
                .categoryCode("Electronics")
                .build()))
            .cost("12.99")
            .price("14.99")
            .dimensions(DimensionsDto.builder()
                .height(1)
                .length(2)
                .width(3)
                .build())
            .images(List.of(ImageDto.builder()
                .url("http://example.com/image1")
                .altText("Image 1")
                .build()))
            .build();

        ProductDto product2 = ProductDto.builder()
            .description("Product 2")
            .categories(List.of(CategoryDto.builder()
                .category("Category 2")
                .description("Category 2 Description")
                .categoryCode("Gifts")
                .build()))
            .cost("12.99")
            .price("14.99")
            .dimensions(DimensionsDto.builder()
                .height(1)
                .length(2)
                .width(3)
                .build())
            .images(List.of(ImageDto.builder()
                .url("http://example.com/image2")
                .altText("Image 2")
                .build()))
            .build();

        ProductDto savedProduct1 = productRepository.save(product1);
        ProductDto savedProduct2 = productRepository.save(product2);
        log.info("Products loaded: {}", productRepository.count());

        // Create orders
        OrderDto order1 = OrderDto.builder()
            .customer(OrderCustomerDto.builder()
                .id(savedCustomer1.getId())
                .name(savedCustomer1.getName())
                .email(savedCustomer1.getEmail())
                .billToAddress(savedCustomer1.getBillToAddress())
                .shipToAddress(savedCustomer1.getShipToAddress())
                .phone(savedCustomer1.getPhone())
                .selectedPaymentMethod(savedCustomer1.getPaymentMethods().getFirst())
                .build())
            .orderStatus(OrderDto.OrderStatusEnum.NEW)
            .shipmentInfo("shipment info")
            .orderLines(List.of(OrderLineDto.builder()
                    .product(OrderProductDto.builder()
                        .id(savedProduct1.getId())
                        .description(product1.getDescription())
                        .price(product1.getPrice())
                        .build())
                    .orderQuantity(1)
                    .shipQuantity(1)
                    .build(),
                OrderLineDto.builder()
                    .product(OrderProductDto.builder()
                        .id(savedProduct2.getId())
                        .description(product2.getDescription())
                        .price(product1.getPrice())
                        .build())
                    .orderQuantity(1)
                    .shipQuantity(1)
                    .build()))
            .build();

        OrderDto order2 = OrderDto.builder()
            .customer(OrderCustomerDto.builder()
                .id(savedCustomer2.getId())
                .name(savedCustomer2.getName())
                .email(savedCustomer2.getEmail())
                .billToAddress(savedCustomer2.getBillToAddress())
                .shipToAddress(savedCustomer2.getShipToAddress())
                .phone(savedCustomer2.getPhone())
                .selectedPaymentMethod(savedCustomer2.getPaymentMethods().get(0))
                .build())
            .orderStatus(OrderDto.OrderStatusEnum.NEW)
            .shipmentInfo("shipment info #2")
            .orderLines(List.of(OrderLineDto.builder()
                    .product(OrderProductDto.builder()
                        .id(savedProduct1.getId())
                        .description(product1.getDescription())
                        .price(product1.getPrice())
                        .build())
                    .orderQuantity(1)
                    .shipQuantity(1)
                    .build(),
                OrderLineDto.builder()
                    .product(OrderProductDto.builder()
                        .id(savedProduct2.getId())
                        .description(product2.getDescription())
                        .price(product1.getPrice())
                        .build())
                    .orderQuantity(1)
                    .shipQuantity(1)
                    .build()))
            .build();

        orderRepository.save(order1);
        orderRepository.save(order2);
    }

    
}
