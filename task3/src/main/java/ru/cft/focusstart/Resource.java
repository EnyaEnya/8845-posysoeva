package ru.cft.focusstart;

import java.util.concurrent.atomic.AtomicLong;

class Resource {
    private final static AtomicLong SEQUENCE = new AtomicLong();
    private long id = SEQUENCE.getAndIncrement();

    long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
