package ru.cft.focusstart.api.dto;

public abstract class AbstractEntityDto {

    protected AbstractEntityDto(Long id) {
        this.id = id;
    }

    private final Long id;

    public Long getId() {
        return id;
    }
}
