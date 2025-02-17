package ch.guru.springframework.apifirst.apifirstserver.jpa.mapper;

import ch.guru.springframework.apifirst.apifirstserver.jpa.bootstrap.DataLoader;
import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Category;
import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Product;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.CategoryRepository;
import ch.guru.springframework.apifirst.model.DimensionsDto;
import ch.guru.springframework.apifirst.model.ImageDto;
import ch.guru.springframework.apifirst.model.ProductCreateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(DataLoader.class)
class ProductMapperTest {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void testMapDtoToToProduct() {

        //fail if no category found
        Category category = categoryRepository.findByCategoryCode("ELECTRONICS").orElseThrow();
        ProductCreateDto productCreateDto = buildProductCreateDto(category.getCategoryCode());

        Product product = productMapper.dtoToProduct(productCreateDto);

        assertAll(
            //test to catch changes, fail test if fields are added
            () -> assertEquals(9, product.getClass().getDeclaredFields().length),
            
            () -> assertNotNull(product, "Product should not be null"),
            
            () -> assertEquals(productCreateDto.getDescription(), product.getDescription()),
            () -> assertEquals(productCreateDto.getCost(), product.getCost()),
            () -> assertEquals(productCreateDto.getPrice(), product.getPrice()),
            () -> assertEquals(productCreateDto.getDimensions().getHeight(), product.getDimensions().getHeight()),
            () -> assertEquals(productCreateDto.getDimensions().getWidth(), product.getDimensions().getWidth()),
            () -> assertEquals(productCreateDto.getDimensions().getLength(), product.getDimensions().getLength()),
            () -> assertEquals(productCreateDto.getImages().getFirst().getUrl(), product.getImages().getFirst().getUrl()),
            () -> assertEquals(productCreateDto.getImages().getFirst().getAltText(), product.getImages().getFirst().getAltText()),
            () -> assertEquals(productCreateDto.getCategories().getFirst(), product.getCategories().getFirst().getCategoryCode())
        );
    }

    ProductCreateDto buildProductCreateDto(String cat) {
        return ProductCreateDto.builder()
            .price("1.0")
            .description("description")
            .images(Collections.singletonList(ImageDto.builder()
                .url("http://example.com/image.jpg")
                .altText("Image Alt Text")
                .build()))
            .categories(Collections.singletonList(cat))
            .cost("1.0")
            .dimensions(DimensionsDto.builder()
                .height(1)
                .length(1)
                .width(1)
                .build())
            .build();
    }
}
