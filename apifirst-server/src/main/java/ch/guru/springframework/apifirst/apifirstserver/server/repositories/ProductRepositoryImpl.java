package ch.guru.springframework.apifirst.apifirstserver.server.repositories;

import ch.guru.springframework.apifirst.model.CategoryDto;
import ch.guru.springframework.apifirst.model.DimensionsDto;
import ch.guru.springframework.apifirst.model.ImageDto;
import ch.guru.springframework.apifirst.model.ProductDto;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    
    private final Map<UUID, ProductDto> entityMap = new HashMap<>();

    @Override
    public <S extends ProductDto> S save(S entity) {

        ProductDto.ProductDtoBuilder builder = ProductDto.builder();

        builder.id(UUID.randomUUID())
            .description(entity.getDescription())
            .cost(entity.getCost())
            .price(entity.getPrice())
            .dateCreated(OffsetDateTime.now())
            .dateUpdated(OffsetDateTime.now());

        if (entity.getCategories() != null) {
            builder.categories(entity.getCategories().stream()
                .map(category -> {
                    return CategoryDto.builder()
                        .id(UUID.randomUUID())
                        .category(category.getCategory())
                        .categoryCode(category.getCategoryCode())
                        .description(category.getDescription())
                        .dateCreated(OffsetDateTime.now())
                        .dateUpdated(OffsetDateTime.now())
                        .build();
                })
                .collect(Collectors.toList()));
        }

        if (entity.getImages() != null) {
            builder.images(entity.getImages().stream()
                .map(image -> {
                    return ImageDto.builder()
                        .id(UUID.randomUUID())
                        .url(image.getUrl())
                        .altText(image.getAltText())
                        .dateCreated(OffsetDateTime.now())
                        .dateUpdated(OffsetDateTime.now())
                        .build();
                })
                .collect(Collectors.toList()));
        }

        if (entity.getDimensions() != null) {
            builder.dimensions(DimensionsDto.builder()
                .length(entity.getDimensions().getLength())
                .width(entity.getDimensions().getWidth())
                .height(entity.getDimensions().getHeight())
                .build());
        }

        ProductDto product = builder.build();

        entityMap.put(product.getId(), product);
        return (S) product;
    }

    @Override
    public <S extends ProductDto> Iterable<S> saveAll(Iterable<S> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
            .map(this::save)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductDto> findById(UUID uuid) {
        return Optional.of(entityMap.get(uuid));
    }

    @Override
    public boolean existsById(UUID uuid) {
        return entityMap.get(uuid) != null;
    }

    @Override
    public Iterable<ProductDto> findAll() {
        return entityMap.values();
    }

    @Override
    public Iterable<ProductDto> findAllById(Iterable<UUID> uuids) {
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
    public void delete(ProductDto entity) {
        entityMap.remove(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {
        uuids.forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends ProductDto> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        entityMap.clear();
    }
}
