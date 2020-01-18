package ru.cft.focusstart.entity;

public abstract class AbstractEntity {

    protected Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
