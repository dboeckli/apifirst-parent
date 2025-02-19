package ch.guru.springframework.apifirst.apifirstserver.jpa.service;

import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Product;
import ch.guru.springframework.apifirst.apifirstserver.jpa.mapper.ProductMapper;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.ProductRepository;
import ch.guru.springframework.apifirst.model.ProductCreateDto;
import ch.guru.springframework.apifirst.model.ProductDto;
import ch.guru.springframework.apifirst.model.ProductPatchDto;
import ch.guru.springframework.apifirst.model.ProductUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Override
    public List<ProductDto> listProducts() {
        return productRepository.findAll().stream()
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

    @Override
    public ProductDto updateProduct(UUID productId, ProductUpdateDto product) {
        Product existingProduct = productRepository.findById(productId).orElseThrow();
        productMapper.updateProduct(product, existingProduct);
        
        return productMapper.productToDto(productRepository.save(existingProduct));
    }

    @Override
    public ProductDto patchProduct(UUID productId, ProductPatchDto product) {
        Product existingProduct = productRepository.findById(productId).orElseThrow();
        productMapper.patchProduct(product, existingProduct);
        
        return productMapper.productToDto(productRepository.save(existingProduct));
    }
}
