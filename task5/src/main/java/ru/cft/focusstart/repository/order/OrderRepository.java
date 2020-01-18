package ru.cft.focusstart.repository.order;

import ru.cft.focusstart.entity.Order;
import ru.cft.focusstart.repository.EntityRepository;

import java.util.List;

public interface OrderRepository extends EntityRepository<Order> {

    List<Order> getByCustomerId(Long id);


}
