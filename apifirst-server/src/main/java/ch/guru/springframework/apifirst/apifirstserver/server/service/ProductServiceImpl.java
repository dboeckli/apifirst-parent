package ch.guru.springframework.apifirst.apifirstserver.server.service;

import ch.guru.springframework.apifirst.apifirstserver.server.repositories.CategoryRepository;
import ch.guru.springframework.apifirst.apifirstserver.server.repositories.ProductRepository;
import ch.guru.springframework.apifirst.model.CategoryDto;
import ch.guru.springframework.apifirst.model.ProductCreateDto;
import ch.guru.springframework.apifirst.model.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    
    private final CategoryRepository categoryRepository;
    
    @Override
    public List<ProductDto> listProducts() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false).toList();
    }

    @Override
    public ProductDto getProductById(UUID productId) {
        return productRepository.findById(productId).orElseThrow();
    }

    @Override
    public ProductDto saveNewProduct(ProductCreateDto productCreateDto) {
        CategoryDto categoryDto = categoryRepository.findByCategoryCode(productCreateDto.getCategories().getFirst()).orElseThrow();
        List<CategoryDto> categories =new ArrayList<>();
        categories.add(categoryDto);
                
        ProductDto product = ProductDto.builder()
            .description(productCreateDto.getDescription())
            .price(productCreateDto.getPrice())
            .cost(productCreateDto.getCost())
            .dimensions(productCreateDto.getDimensions())
            .images(productCreateDto.getImages())
            .categories(categories)
            .build();
        return productRepository.save(product);
    }


    private List<CategoryDto> mapCategories(List<String> categoryNames) {
        if (categoryNames == null) {
            return new ArrayList<>();
        }
        return categoryNames.stream()
            .map(name -> CategoryDto.builder()
                .category(name)
                .build())
            .collect(Collectors.toList());
    }
}
