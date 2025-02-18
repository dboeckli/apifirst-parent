package ch.guru.springframework.apifirst.apifirstserver.jpa.controller;

import ch.guru.springframework.apifirst.apifirstserver.jpa.service.ProductService;
import ch.guru.springframework.apifirst.model.ProductCreateDto;
import ch.guru.springframework.apifirst.model.ProductDto;
import ch.guru.springframework.apifirst.model.ProductUpdateDto;
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
    public ResponseEntity<List<ProductDto>> listProducts() {
        return ResponseEntity.ok(productService.listProducts());
    }

    @PostMapping
    public ResponseEntity<Void> saveNewProduct(@RequestBody ProductCreateDto product){
        ProductDto savedProduct = productService.saveNewProduct(product);

        // we are returning the location in the header location field of the HTTP response.
        UriComponents uriComponents = UriComponentsBuilder.fromPath(PRODUCT_BASE_URL + "/{product_id}")
                .buildAndExpand(savedProduct.getId());

        return ResponseEntity.created(URI.create(uriComponents.getPath())).build();
    }
    
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("productId") UUID productId, @RequestBody ProductUpdateDto updatedProduct) {
        return ResponseEntity.ok(productService.updateProduct(productId, updatedProduct));
    }
    

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> geProductById(@PathVariable("productId") UUID productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }
    
    
    
}
