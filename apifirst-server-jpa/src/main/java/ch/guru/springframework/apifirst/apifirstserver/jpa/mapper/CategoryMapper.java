package ch.guru.springframework.apifirst.apifirstserver.jpa.mapper;

import ch.guru.springframework.apifirst.apifirstserver.jpa.domain.Category;
import ch.guru.springframework.apifirst.model.CategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CategoryMapper {
    
    CategoryDto categoryToCategoryDto(Category category);

    @Mapping(target = "products", ignore = true)
    Category categoryDtoToCategory(CategoryDto categoryDto);
}
