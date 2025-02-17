package ch.guru.springframework.apifirst.apifirstserver.jpa.service;

import ch.guru.springframework.apifirst.apifirstserver.jpa.mapper.ProductMapper;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.ProductRepository;
import ch.guru.springframework.apifirst.model.ProductCreateDto;
import ch.guru.springframework.apifirst.model.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Override
    public List<ProductDto> listProducts() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
            .map(productMapper::productToDto)
            .toList();
    }

    @Override
    public ProductDto getProductById(UUID productId) {
        return productMapper.productToDto(productRepository.findById(productId).orElseThrow());
    }

    @Override
    public ProductDto saveNewProduct(ProductCreateDto product) {
        return productMapper.productToDto(productRepository.save(productMapper.dtoToProduct(product)));
    }
}
