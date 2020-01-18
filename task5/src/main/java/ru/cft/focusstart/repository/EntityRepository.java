package ru.cft.focusstart.repository;

import ru.cft.focusstart.entity.AbstractEntity;

import java.util.Optional;

public interface EntityRepository<E extends AbstractEntity> {

    void add(E entity);

    Optional<E> getById(Long id);

    void update(E entity);

    void delete(long id);
}
