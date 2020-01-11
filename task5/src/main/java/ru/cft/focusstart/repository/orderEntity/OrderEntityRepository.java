package ru.cft.focusstart.repository.orderEntity;

import ru.cft.focusstart.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderEntityRepository {

    void add(Order Order);

    Optional<Order> getById(Long id);

    List<Order> getByUserId(Long id);

    void update(Order order);

    void delete(Order Order);
}
