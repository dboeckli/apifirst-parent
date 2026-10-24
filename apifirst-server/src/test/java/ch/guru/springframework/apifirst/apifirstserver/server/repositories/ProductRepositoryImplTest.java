package ch.guru.springframework.apifirst.apifirstserver.server.repositories;

import ch.guru.springframework.apifirst.model.CategoryDto;
import ch.guru.springframework.apifirst.model.DimensionsDto;
import ch.guru.springframework.apifirst.model.ImageDto;
import ch.guru.springframework.apifirst.model.ProductDto;
import org.junit.jupiter.api.Test;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRepositoryImplTest extends AbstractRepositoryContract<ProductDto> {

    private final ProductRepositoryImpl repository = new ProductRepositoryImpl();

    @Override
    protected CrudRepository<ProductDto, UUID> createRepository() {
        return new ProductRepositoryImpl();
    }

    @Override
    protected ProductDto newEntity() {
        return ProductDto.builder()
            .description("Product")
            .cost("12.99")
            .price("14.99")
            .categories(List
                .of(CategoryDto.builder().category("Category").categoryCode("CODE").description("Description").build()))
            .images(List.of(ImageDto.builder().url("http://example.com/image").altText("Image").build()))
            .dimensions(DimensionsDto.builder().height(1).length(2).width(3).build())
            .build();
    }

    @Override
    protected UUID getId(ProductDto entity) {
        return entity.getId();
    }

    @Test
    void saveMapsNestedCollections() {
        ProductDto saved = repository.save(newEntity());

        assertThat(saved.getCategories()).hasSize(1);
        assertThat(saved.getCategories().getFirst().getId()).isNotNull();
        assertThat(saved.getImages()).hasSize(1);
        assertThat(saved.getImages().getFirst().getId()).isNotNull();
        assertThat(saved.getDimensions().getHeight()).isEqualTo(1);
    }

    @Test
    void saveWithoutOptionalFields() {
        ProductDto product = ProductDto.builder()
            .description("Product")
            .price("14.99")
            .categories(null)
            .images(null)
            .dimensions(null)
            .build();

        ProductDto saved = repository.save(product);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCategories()).isNull();
        assertThat(saved.getImages()).isNull();
        assertThat(saved.getDimensions()).isNull();
    }

}
