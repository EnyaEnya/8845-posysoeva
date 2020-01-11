package ru.cft.focusstart.repository.order;

import ru.cft.focusstart.entity.Order;
import ru.cft.focusstart.entity.OrderEntity;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    void add(Order Order);

    Optional<Order> getById(Long id);

    List<Order> getByCustomerId(Long id);

    void update(Order order);

    void insertOrderEntity(OrderEntity orderEntity);

    void updateOrderEntity(OrderEntity orderEntity);

    void delete(Order Order);
}
