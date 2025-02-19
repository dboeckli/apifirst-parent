package ch.guru.springframework.apifirst.apifirstserver.jpa.mapper;

import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Product;
import ch.guru.springframework.apifirst.model.ProductCreateDto;
import ch.guru.springframework.apifirst.model.ProductDto;
import ch.guru.springframework.apifirst.model.ProductPatchDto;
import ch.guru.springframework.apifirst.model.ProductUpdateDto;
import org.mapstruct.*;

@Mapper
@DecoratedWith(ProductMapperDecorator.class)
public interface ProductMapper {
    @Mapping(target = "categories", ignore = true)
    ProductPatchDto productToPatchDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateUpdated", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "images", ignore = true)
    @BeanMapping(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void patchProduct(ProductPatchDto productPatchDto, @MappingTarget Product target);

    @Mapping(target = "categories", ignore = true)
    ProductUpdateDto productToUpdateDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateUpdated", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Product dtoToProduct(ProductUpdateDto productUpdateDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateUpdated", ignore = true)
    @Mapping(target = "categories", ignore = true)
    void updateProduct(ProductUpdateDto product, @MappingTarget Product target);

    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateUpdated", ignore = true)
    Product dtoToProduct(ProductDto productDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateUpdated", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Product dtoToProduct(ProductCreateDto productCreateDto);

    ProductDto productToDto(Product product);
}
