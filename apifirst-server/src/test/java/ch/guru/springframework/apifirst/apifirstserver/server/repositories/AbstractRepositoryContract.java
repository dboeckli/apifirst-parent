package ch.guru.springframework.apifirst.apifirstserver.server.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

abstract class AbstractRepositoryContract<T> {

    private CrudRepository<T, UUID> repository;

    @BeforeEach
    void setUpRepository() {
        repository = createRepository();
    }

    protected abstract CrudRepository<T, UUID> createRepository();

    protected abstract T newEntity();

    protected abstract UUID getId(T entity);

    @Test
    void saveAssignsId() {
        T saved = repository.save(newEntity());

        assertThat(getId(saved)).isNotNull();
        assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    void saveAllAndFindAll() {
        Iterable<T> saved = repository.saveAll(List.of(newEntity(), newEntity()));

        assertThat(saved).hasSize(2);
        assertThat(repository.findAll()).hasSize(2);
    }

    @Test
    void findByIdAndExistsById() {
        T saved = repository.save(newEntity());

        assertThat(repository.findById(getId(saved))).contains(saved);
        assertThat(repository.existsById(getId(saved))).isTrue();
    }

    @Test
    void findByIdReturnsEmptyForUnknownId() {
        assertThat(repository.findById(UUID.randomUUID())).isEmpty();
        assertThat(repository.existsById(UUID.randomUUID())).isFalse();
    }

    @Test
    void findAllByIdSkipsUnknownIds() {
        T saved = repository.save(newEntity());

        assertThat(repository.findAllById(List.of(getId(saved), UUID.randomUUID()))).containsExactly(saved);
    }

    @Test
    void deleteByIdRemovesEntity() {
        T saved = repository.save(newEntity());

        repository.deleteById(getId(saved));

        assertThat(repository.count()).isZero();
    }

    @Test
    void deleteRemovesEntity() {
        T saved = repository.save(newEntity());

        repository.delete(saved);

        assertThat(repository.count()).isZero();
    }

    @Test
    void deleteAllByIdRemovesEntities() {
        T first = repository.save(newEntity());
        T second = repository.save(newEntity());

        repository.deleteAllById(List.of(getId(first), getId(second)));

        assertThat(repository.count()).isZero();
    }

    @Test
    void deleteAllIterableRemovesEntities() {
        T first = repository.save(newEntity());
        T second = repository.save(newEntity());

        repository.deleteAll(List.of(first, second));

        assertThat(repository.count()).isZero();
    }

    @Test
    void deleteAllRemovesEverything() {
        repository.save(newEntity());
        repository.save(newEntity());

        repository.deleteAll();

        assertThat(repository.count()).isZero();
    }

}
