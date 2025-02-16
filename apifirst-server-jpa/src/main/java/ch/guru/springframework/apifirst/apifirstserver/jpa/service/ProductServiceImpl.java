package ch.guru.springframework.apifirst.apifirstserver.jpa.service;

import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.ProductRepository;
import ch.guru.springframework.apifirst.model.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    
    @Override
    public List<ProductDto> listProducts() {
        // TODO. Implement proper error handling and validation
        //return StreamSupport.stream(productRepository.findAll().spliterator(), false).toList();
        return Collections.emptyList();
    }

    @Override
    public ProductDto getProductById(UUID productId) {
        // TODO. Implement proper error handling and validation
        //return productRepository.findById(productId).orElseThrow();
        return null;
    }

    @Override
    public ProductDto saveNewProduct(ProductDto product) {
        // TODO. Implement proper error handling and validation
        //return productRepository.save(product);
        return null;
    }
}
