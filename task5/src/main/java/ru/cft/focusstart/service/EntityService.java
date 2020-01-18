package ru.cft.focusstart.service;

import ru.cft.focusstart.api.dto.AbstractEntityDto;

public interface EntityService<D extends AbstractEntityDto> {

    D getById(Long id);

    void delete(Long id);

}
