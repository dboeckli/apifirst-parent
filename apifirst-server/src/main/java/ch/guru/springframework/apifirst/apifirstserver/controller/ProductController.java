package ch.guru.springframework.apifirst.apifirstserver.controller;

import ch.guru.springframework.apifirst.apifirstserver.service.ProductService;
import ch.guru.springframework.apifirst.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(ProductController.PRODUCT_BASE_URL)
public class ProductController {

    public static final String PRODUCT_BASE_URL = "/v1/products";

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> listProducts() {
        return ResponseEntity.ok(productService.listProducts());
    }

    @PostMapping
    public ResponseEntity<Void> saveNewProduct(@RequestBody Product product){
        Product savedProduct = productService.saveNewProduct(product);

        // we are returning the location in the header location field of the HTTP response.
        UriComponents uriComponents = UriComponentsBuilder.fromPath(PRODUCT_BASE_URL + "/{product_id}")
                .buildAndExpand(savedProduct.getId());

        return ResponseEntity.created(URI.create(uriComponents.getPath())).build();
    }
    

    @GetMapping("/{productId}")
    public ResponseEntity<Product> geProductById(@PathVariable("productId") UUID productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }
    
    
    
}
