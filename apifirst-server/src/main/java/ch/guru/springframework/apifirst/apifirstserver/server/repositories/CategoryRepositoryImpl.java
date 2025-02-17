package ch.guru.springframework.apifirst.apifirstserver.server.repositories;

import ch.guru.springframework.apifirst.model.CategoryDto;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final Map<UUID, CategoryDto> entityMap = new HashMap<>();

    @Override
    public <S extends CategoryDto> S save(S entity) {
        UUID id = UUID.randomUUID();

        CategoryDto.CategoryDtoBuilder builder1 = CategoryDto.builder();

        builder1.id(id);

        CategoryDto category = builder1
            .category(entity.getCategory())
            .categoryCode(entity.getCategoryCode())
            .description(entity.getDescription())
            .dateCreated(OffsetDateTime.now())
            .dateUpdated(OffsetDateTime.now())
            .build();

        entityMap.put(id, category);

        return (S) category;
    }

    @Override
    public <S extends CategoryDto> Iterable<S> saveAll(Iterable<S> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
            .map(this::save)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<CategoryDto> findById(UUID uuid) {
        return Optional.of(entityMap.get(uuid));
    }

    @Override
    public boolean existsById(UUID uuid) {
        return entityMap.get(uuid) != null;
    }

    @Override
    public Iterable<CategoryDto> findAll() {
        return entityMap.values();
    }

    @Override
    public Iterable<CategoryDto> findAllById(Iterable<UUID> uuids) {
        return StreamSupport.stream(uuids.spliterator(), false)
            .map(this::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return entityMap.size();
    }

    @Override
    public void deleteById(UUID uuid) {
        entityMap.remove(uuid);
    }

    @Override
    public void delete(CategoryDto entity) {
        entityMap.remove(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {
        uuids.forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends CategoryDto> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        entityMap.clear();
    }

    @Override
    public Optional<CategoryDto> findByCategoryCode(String categoryCode) {
        return entityMap.values().stream()
            .filter(category -> category.getCategoryCode().equals(categoryCode))
            .findFirst();
    }
}
