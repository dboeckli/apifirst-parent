package ch.guru.springframework.apifirst.apifirstserver.jpa.controller;

import ch.guru.springframework.apifirst.apifirstserver.jpa.bootstrap.DataLoader;
import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Product;
import ch.guru.springframework.apifirst.apifirstserver.jpa.mapper.ProductMapper;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.CategoryRepository;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.ProductRepository;
import ch.guru.springframework.apifirst.model.DimensionsDto;
import ch.guru.springframework.apifirst.model.ImageDto;
import ch.guru.springframework.apifirst.model.ProductCreateDto;
import ch.guru.springframework.apifirst.model.ProductUpdateDto;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(DataLoader.class)
@Slf4j
class ProductControllerTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;
    
    @Autowired
    ProductMapper productMapper;

    @Autowired
    WebApplicationContext wac;

    public MockMvc mockMvc;

    @Autowired
    Filter validationFilter;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper.configure(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION, true);
        
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(validationFilter)
                .build();
    }

    @Test
    @Order(1)
    void testListProducts() throws Exception {
        mockMvc.perform(get(ProductController.PRODUCT_BASE_URL)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", greaterThan(0)));
    }

    @Test
    @Order(2)
    void testGetProductById() throws Exception {
        Product testProduct = productRepository.findAll().getFirst();

        mockMvc.perform(get(ProductController.PRODUCT_BASE_URL + "/{prodcutId}", testProduct.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(testProduct.getId().toString()));
    }

    @Test
    @Order(3)
    void testCreateProduct() throws Exception {
        String categoryName = categoryRepository.findAll().getFirst().getCategory();
        
        ProductCreateDto newProduct = ProductCreateDto.builder()
                .description("New Product")
                .cost("5.00")
                .price("8.95")
                .categories(List.of(categoryName))
                .images(Collections.singletonList(ImageDto.builder()
                    .url("http://example.com/image.jpg")
                    .altText("Image Alt Text")
                    .build()))
                .dimensions(DimensionsDto.builder()
                        .length(10)
                        .width(10)
                        .height(10)
                        .build())
                .build();

        MvcResult result = mockMvc.perform(post(ProductController.PRODUCT_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        String locationHeader = result.getResponse().getHeader("Location");
        log.info("Location header: {}", locationHeader);

        // Extract the URI from the location header
        URI locationUri = new URI(locationHeader);
        String path = locationUri.getPath();
        log.info("Extracted path: {}", path);

        // Perform GET request using the extracted path
        mockMvc.perform(get(path)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("New Product"))
                .andExpect(jsonPath("$.cost").value("5.00"))
                .andExpect(jsonPath("$.price").value("8.95"));

    }

    @Transactional
    @Test
    void testUpdateProduct() throws Exception {
        Product product = productRepository.findAll().getFirst();

        ProductUpdateDto productUpdateDto = productMapper.productToUpdateDto(product);
        productUpdateDto.setDescription("Updated Description");

        mockMvc.perform(put(ProductController.PRODUCT_BASE_URL + "/{productId}", product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productUpdateDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.description", equalTo("Updated Description")));
    }

}
