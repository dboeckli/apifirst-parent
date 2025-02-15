package ch.guru.springframework.apifirst.apifirstserver.service;

import ch.guru.springframework.apifirst.model.ProductDto;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    
    List<ProductDto> listProducts();

    ProductDto getProductById(UUID productId);

    ProductDto saveNewProduct(ProductDto product);
}
