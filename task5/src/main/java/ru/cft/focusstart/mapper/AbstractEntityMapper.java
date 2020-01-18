package ru.cft.focusstart.mapper;

import ru.cft.focusstart.api.dto.AbstractEntityDto;
import ru.cft.focusstart.entity.AbstractEntity;

public abstract class AbstractEntityMapper<E extends AbstractEntity, D extends AbstractEntityDto> {

    public abstract D toDto(E entity);

}
