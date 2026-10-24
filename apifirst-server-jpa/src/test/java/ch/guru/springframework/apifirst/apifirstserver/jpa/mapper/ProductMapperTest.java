package ch.guru.springframework.apifirst.apifirstserver.jpa.mapper;

import ch.guru.springframework.apifirst.apifirstserver.jpa.bootstrap.DataLoader;
import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Category;
import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Image;
import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Product;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.CategoryRepository;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.ImageRepository;
import ch.guru.springframework.apifirst.model.DimensionsDto;
import ch.guru.springframework.apifirst.model.ImageDto;
import ch.guru.springframework.apifirst.model.ProductCreateDto;
import ch.guru.springframework.apifirst.model.ProductImagePatchDto;
import ch.guru.springframework.apifirst.model.ProductImageUpdateDto;
import ch.guru.springframework.apifirst.model.ProductDto;
import ch.guru.springframework.apifirst.model.ProductPatchDto;
import ch.guru.springframework.apifirst.model.ProductUpdateDto;
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

    @Autowired
    ImageRepository imageRepository;

    @Test
    void testMapDtoToToProduct() {

        // fail if no category found
        Category category = categoryRepository.findByCategoryCode("ELECTRONICS").orElseThrow();
        ProductCreateDto productCreateDto = buildProductCreateDto(category.getCategoryCode());

        Product product = productMapper.dtoToProduct(productCreateDto);

        assertAll(
                // test to catch changes, fail test if fields are added
                () -> assertEquals(9,
                        java.util.Arrays.stream(product.getClass().getDeclaredFields())
                            .filter(field -> !java.lang.reflect.Modifier.isStatic(field.getModifiers()))
                            .count()),

                () -> assertNotNull(product, "Product should not be null"),

                () -> assertEquals(productCreateDto.getDescription(), product.getDescription()),
                () -> assertEquals(productCreateDto.getCost(), product.getCost()),
                () -> assertEquals(productCreateDto.getPrice(), product.getPrice()),
                () -> assertEquals(productCreateDto.getDimensions().getHeight(), product.getDimensions().getHeight()),
                () -> assertEquals(productCreateDto.getDimensions().getWidth(), product.getDimensions().getWidth()),
                () -> assertEquals(productCreateDto.getDimensions().getLength(), product.getDimensions().getLength()),
                () -> assertEquals(productCreateDto.getImages().getFirst().getUrl(),
                        product.getImages().getFirst().getUrl()),
                () -> assertEquals(productCreateDto.getImages().getFirst().getAltText(),
                        product.getImages().getFirst().getAltText()),
                () -> assertEquals(productCreateDto.getCategories().getFirst(),
                        product.getCategories().getFirst().getCategoryCode()));
    }

    @Test
    void testProductToUpdateDto() {
        Product product = Product.builder().description("description").price("1.0").build();
        ProductUpdateDto productUpdateDto = productMapper.productToUpdateDto(product);
        assertAll(() -> assertNotNull(productUpdateDto),
                () -> assertEquals("description", productUpdateDto.getDescription()),
                () -> assertEquals("1.0", productUpdateDto.getPrice()));
    }

    @Test
    void testProductToUpdateDtoWithCategoriesAndImages() {
        Product product = Product.builder()
            .description("description")
            .price("1.0")
            .categories(Collections
                .singletonList(Category.builder().category("ELECTRONICS").categoryCode("ELECTRONICS_CODE").build()))
            .images(Collections.singletonList(Image.builder().url("http://example.com/image.jpg").build()))
            .build();
        ProductUpdateDto productUpdateDto = productMapper.productToUpdateDto(product);
        assertAll(() -> assertNotNull(productUpdateDto),
                () -> assertEquals("description", productUpdateDto.getDescription()),
                () -> assertEquals("1.0", productUpdateDto.getPrice()),
                () -> assertNotNull(productUpdateDto.getCategories()),
                () -> assertEquals(1, productUpdateDto.getCategories().size()),
                () -> assertTrue(productUpdateDto.getCategories().stream().allMatch(String.class::isInstance)),
                () -> assertEquals("ELECTRONICS_CODE", productUpdateDto.getCategories().getFirst()),
                () -> assertEquals(1, productUpdateDto.getImages().size()),
                () -> assertEquals("http://example.com/image.jpg", productUpdateDto.getImages().getFirst().getUrl()));
    }

    @Test
    void testMapUpdateDtoToProduct() {
        Category category = categoryRepository.findByCategoryCode("ELECTRONICS").orElseThrow();
        Image image = imageRepository
            .save(Image.builder().url("http://example.com/existing").altText("Existing").build());

        ProductUpdateDto productUpdateDto = ProductUpdateDto.builder()
            .description("description")
            .price("1.0")
            .categories(Collections.singletonList(category.getCategoryCode()))
            .images(Collections.singletonList(ProductImageUpdateDto.builder()
                .id(image.getId())
                .url("http://example.com/updated")
                .altText("Updated")
                .build()))
            .build();

        Product product = productMapper.dtoToProduct(productUpdateDto);

        assertAll(() -> assertNotNull(product), () -> assertEquals("description", product.getDescription()),
                () -> assertEquals(1, product.getCategories().size()),
                () -> assertEquals(category.getCategoryCode(), product.getCategories().getFirst().getCategoryCode()),
                () -> assertEquals(1, product.getImages().size()),
                () -> assertEquals("http://example.com/updated", product.getImages().getFirst().getUrl()));
    }

    @Test
    void testMapNullUpdateDtoToProduct() {
        assertNull(productMapper.dtoToProduct((ProductUpdateDto) null));
    }

    @Test
    void testMapNullCreateDtoToProduct() {
        assertNull(productMapper.dtoToProduct((ProductCreateDto) null));
    }

    @Test
    void testUpdateProduct() {
        Category category = categoryRepository.findByCategoryCode("ELECTRONICS").orElseThrow();
        Image image = imageRepository
            .save(Image.builder().url("http://example.com/existing").altText("Existing").build());
        Product target = Product.builder().description("old").price("1.0").build();

        ProductUpdateDto productUpdateDto = ProductUpdateDto.builder()
            .description("updated")
            .price("2.0")
            .categories(Collections.singletonList(category.getCategoryCode()))
            .images(Collections.singletonList(
                    ProductImageUpdateDto.builder().id(image.getId()).url("http://example.com/updated").build()))
            .build();

        productMapper.updateProduct(productUpdateDto, target);

        assertAll(() -> assertEquals("updated", target.getDescription()),
                () -> assertEquals(1, target.getCategories().size()), () -> assertEquals(1, target.getImages().size()),
                () -> assertEquals("http://example.com/updated", target.getImages().getFirst().getUrl()));
    }

    @Test
    void testProductToPatchDtoWithoutCategoriesReturnsNull() {
        Product product = Product.builder().description("description").price("1.0").build();

        assertNull(productMapper.productToPatchDto(product));
        assertNull(productMapper.productToPatchDto(null));
    }

    @Test
    void testPatchProduct() {
        Category category = categoryRepository.findByCategoryCode("ELECTRONICS").orElseThrow();
        Image image = imageRepository
            .save(Image.builder().url("http://example.com/existing").altText("Existing").build());
        Product target = Product.builder().description("old").images(Collections.singletonList(image)).build();

        ProductPatchDto productPatchDto = ProductPatchDto.builder()
            .description("patched")
            .categories(Collections.singletonList(category.getCategoryCode()))
            .images(Collections.singletonList(
                    ProductImagePatchDto.builder().id(image.getId()).url("http://example.com/patched").build()))
            .build();

        productMapper.patchProduct(productPatchDto, target);

        assertAll(() -> assertEquals("patched", target.getDescription()),
                () -> assertEquals(1, target.getCategories().size()),
                () -> assertEquals("http://example.com/patched", target.getImages().getFirst().getUrl()));
    }

    @Test
    void testProductToUpdateDtoWithNull() {
        assertNull(productMapper.productToUpdateDto(null));
    }

    @Test
    void testProductToPatchDtoWithCategories() {
        Product product = Product.builder()
            .description("description")
            .price("1.0")
            .categories(Collections
                .singletonList(Category.builder().category("ELECTRONICS").categoryCode("ELECTRONICS_CODE").build()))
            .build();

        ProductPatchDto productPatchDto = productMapper.productToPatchDto(product);

        assertAll(() -> assertNotNull(productPatchDto),
                () -> assertEquals("description", productPatchDto.getDescription()),
                () -> assertEquals(1, productPatchDto.getCategories().size()),
                () -> assertEquals("ELECTRONICS_CODE", productPatchDto.getCategories().getFirst()));
    }

    @Test
    void testUpdateProductWithoutImages() {
        Product target = Product.builder().description("old").price("1.0").build();

        ProductUpdateDto productUpdateDto = ProductUpdateDto.builder().description("updated").price("2.0").build();

        productMapper.updateProduct(productUpdateDto, target);

        assertAll(() -> assertEquals("updated", target.getDescription()), () -> assertNotNull(target.getImages()),
                () -> assertTrue(target.getImages().isEmpty()));
    }

    @Test
    void testProductDtoConversions() {
        ProductDto productDto = ProductDto.builder()
            .description("description")
            .price("1.0")
            .categories(Collections.emptyList())
            .images(Collections.emptyList())
            .build();

        Product product = productMapper.dtoToProduct(productDto);
        ProductDto mapped = productMapper
            .productToDto(Product.builder().description("description").price("1.0").build());

        assertAll(() -> assertNotNull(product), () -> assertEquals("description", product.getDescription()),
                () -> assertNotNull(mapped), () -> assertEquals("description", mapped.getDescription()));
    }

    ProductCreateDto buildProductCreateDto(String cat) {
        return ProductCreateDto.builder()
            .price("1.0")
            .description("description")
            .images(Collections.singletonList(
                    ImageDto.builder().url("http://example.com/image.jpg").altText("Image Alt Text").build()))
            .categories(Collections.singletonList(cat))
            .cost("1.0")
            .dimensions(DimensionsDto.builder().height(1).length(1).width(1).build())
            .build();
    }

}
