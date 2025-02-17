package ch.guru.springframework.apifirst.apifirstserver.server.repositories;

import ch.guru.springframework.apifirst.model.CategoryDto;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends CrudRepository<CategoryDto, UUID> {

    Optional<CategoryDto> findByCategoryCode(String categoryCode);
    
}
