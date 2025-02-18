package ch.guru.springframework.apifirst.apifirstserver.jpa.mapper;

import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Category;
import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Image;
import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Product;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.CategoryRepository;
import ch.guru.springframework.apifirst.apifirstserver.jpa.repositories.ImageRepository;
import ch.guru.springframework.apifirst.model.ProductCreateDto;
import ch.guru.springframework.apifirst.model.ProductDto;
import ch.guru.springframework.apifirst.model.ProductUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

public abstract class ProductMapperDecorator implements ProductMapper {

    @Autowired
    @Qualifier("delegate")
    private ProductMapper productMapperDelegate;

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageMapper imageMapper;

    @Override
    public ProductUpdateDto productToUpdateDto(Product product) {
        if (product != null && product.getCategories() != null) {
                List<String> categoryCodes = new ArrayList<>();

                product.getCategories().forEach(category -> categoryCodes.add(category.getCategoryCode()));

                ProductUpdateDto productUpdateDto = productMapperDelegate.productToUpdateDto(product);
                productUpdateDto.setCategories(categoryCodes);

                return productUpdateDto;
            }


        return null;
    }

    @Override
    public Product dtoToProduct(ProductUpdateDto productUpdateDto) {
        if (productUpdateDto != null) {
            Product product = productMapperDelegate.dtoToProduct(productUpdateDto);

            if (productUpdateDto.getCategories() != null) {
                List<Category> categories = categoryCodesToCategories(productUpdateDto.getCategories());

                product.setCategories(categories);
            }

            if (productUpdateDto.getImages() != null) {
                product.setImages(new ArrayList<>());

                productUpdateDto.getImages().forEach(imageDto -> {
                    if (imageDto.getId() != null ) {
                        imageRepository.findById(imageDto.getId()).ifPresent(image -> {
                            Image existingImage = imageRepository.findById(imageDto.getId()).get();
                            imageMapper.updateImage(imageDto, existingImage);
                            product.getImages().add(existingImage);
                        });
                    }
                });
            }

            return product;
        }

        return null;
    }

    @Override
    public void updateProduct(ProductUpdateDto product, Product target) {
        productMapperDelegate.updateProduct(product, target);

        if (product.getImages() != null) {
            product.getImages().forEach(imageDto -> {
                target.setImages(new ArrayList<>());

                if (imageDto.getId() != null) {
                    imageRepository.findById(imageDto.getId()).ifPresent(image -> {
                        Image existingImage = imageRepository.findById(imageDto.getId()).get();
                        imageMapper.updateImage(imageDto, existingImage);
                        target.getImages().add(existingImage);
                    });
                }
            });
        } else {
            target.setImages(new ArrayList<>());
        }

        if (product.getCategories() != null) {
            List<Category> categories = categoryCodesToCategories(product.getCategories());
            target.setCategories(categories);
        }
    }

    //list of string to list of category
    private List<Category> categoryCodesToCategories(List<String> categoryCodes) {
        List<Category> categories = new ArrayList<>();

        categoryCodes.forEach(categoryCode -> categoryRepository.findByCategoryCode(categoryCode).ifPresent(categories::add));

        return categories;
    }

    @Override
    public Product dtoToProduct(ProductDto productDto) {
        return productMapperDelegate.dtoToProduct(productDto);
    }

    @Override
    public Product dtoToProduct(ProductCreateDto productCreateDto) {
        if (productCreateDto != null) {
            Product product = productMapperDelegate.dtoToProduct(productCreateDto);

            if (productCreateDto.getCategories() != null) {
                List<Category> categories = new ArrayList<>();

                productCreateDto.getCategories().forEach(categoryCode -> categoryRepository.findByCategoryCode(categoryCode).ifPresent(categories::add));
                product.setCategories(categories);

                return product;
            }
        }
        return null;
    }

    @Override
    public ProductDto productToDto(Product product) {
        return productMapperDelegate.productToDto(product);
    }
}    
