package ch.guru.springframework.apifirst.apifirstserver.service;

import ch.guru.springframework.apifirst.model.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    
    List<Product> listProducts();

    Product getProductById(UUID productId);

    Product saveNewProduct(Product product);
}
