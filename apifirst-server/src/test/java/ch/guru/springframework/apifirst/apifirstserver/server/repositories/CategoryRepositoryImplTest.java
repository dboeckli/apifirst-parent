package ch.guru.springframework.apifirst.apifirstserver.server.repositories;

import ch.guru.springframework.apifirst.model.CategoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryRepositoryImplTest extends AbstractRepositoryContract<CategoryDto> {

    private final CategoryRepositoryImpl repository = new CategoryRepositoryImpl();

    @Override
    protected CrudRepository<CategoryDto, UUID> createRepository() {
        return new CategoryRepositoryImpl();
    }

    @Override
    protected CategoryDto newEntity() {
        return CategoryDto.builder().category("Category").categoryCode("CODE").description("Description").build();
    }

    @Override
    protected UUID getId(CategoryDto entity) {
        return entity.getId();
    }

    @Test
    void findByCategoryCodeReturnsMatchingEntity() {
        CategoryDto saved = repository.save(newEntity());

        assertThat(repository.findByCategoryCode("CODE")).contains(saved);
        assertThat(repository.findByCategoryCode("UNKNOWN")).isEmpty();
    }

}
