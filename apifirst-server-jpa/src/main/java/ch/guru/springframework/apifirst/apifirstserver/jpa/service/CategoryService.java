package ch.guru.springframework.apifirst.apifirstserver.jpa.service;

import ch.guru.springframework.apifirst.model.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> listCategories();
    
}
