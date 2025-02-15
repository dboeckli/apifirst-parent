package ch.guru.springframework.apifirst.apifirstserver.controller;

import ch.guru.springframework.apifirst.apifirstserver.repositories.ProductRepository;
import ch.guru.springframework.apifirst.model.Category;
import ch.guru.springframework.apifirst.model.Dimentions;
import ch.guru.springframework.apifirst.model.Image;
import ch.guru.springframework.apifirst.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.util.Arrays;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Slf4j
class ProductControllerTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    WebApplicationContext wac;

    public MockMvc mockMvc;

    @Autowired
    Filter validationFilter;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(validationFilter)
                .build();
    }

    @Test
    void testListProducts() throws Exception {
        mockMvc.perform(get(ProductController.PRODUCT_BASE_URL)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", greaterThan(0)));
    }

    @Test
    void testGetProductById() throws Exception {
        Product testProduct = productRepository.findAll().iterator().next();

        mockMvc.perform(get(ProductController.PRODUCT_BASE_URL + "/{prodcutId}", testProduct.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(testProduct.getId().toString()));
    }

    @Test
    void testCreateProduct() throws Exception {
        Product newProduct = Product.builder()
                .description("New Product")
                .cost("5.00")
                .price("8.95")
                .categories(Arrays.asList(Category.builder()
                        .category("New Category")
                        .description("New Category Description")
                        .build()))
                .images(Arrays.asList(Image.builder()
                        .url("http://example.com/image.jpg")
                        .altText("Image Alt Text")
                        .build()))
                .dimentions(Dimentions.builder()
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

}
