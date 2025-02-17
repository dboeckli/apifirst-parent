package ch.guru.springframework.apifirst.apifirstserver.jpa.mapper;

import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Image;
import ch.guru.springframework.apifirst.model.ProductImageUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface ImageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "dateUpdated", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    void updateImage(ProductImageUpdateDto imageUpdateDto, @MappingTarget Image productImage);
    
}
