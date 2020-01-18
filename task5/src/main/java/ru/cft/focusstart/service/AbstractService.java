package ru.cft.focusstart.service;

import ru.cft.focusstart.api.dto.AbstractEntityDto;
import ru.cft.focusstart.entity.AbstractEntity;
import ru.cft.focusstart.exception.ObjectNotFoundException;
import ru.cft.focusstart.mapper.AbstractEntityMapper;
import ru.cft.focusstart.repository.EntityRepository;

import static ru.cft.focusstart.service.validation.Validator.checkNotNull;

public abstract class AbstractService<E extends AbstractEntity,
        D extends AbstractEntityDto,
        R extends EntityRepository<E>,
        M extends  AbstractEntityMapper<E, D>> implements EntityService<D> {

    protected abstract R getRepository();

    protected abstract M getMapper();

    protected E getEntity(Long id) {
        return getRepository().getById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Entity with id %s not found", id)));
    }

    @Override
    public D getById(Long id) {
        checkNotNull("id", id);
        E entity = getEntity(id);
        M mapper = getMapper();
        return mapper.toDto(entity);
    }

    @Override
    public void delete(Long id) {
        checkNotNull("id", id);
        getRepository().delete(id);
    }
}
