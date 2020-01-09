package ru.cft.focusstart.repository.order;

import ru.cft.focusstart.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    void add(Order Order);

    Optional<Order> getById(Long id);

    List<Order> getByUserId(Long id);

    void update(Order Order);

    void delete(Order Order);
}
