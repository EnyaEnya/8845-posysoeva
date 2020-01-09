package ru.cft.focusstart.repository.order;

import ru.cft.focusstart.entity.Order;

import java.util.List;
import java.util.Optional;

public class JdbcOrderRepository implements OrderRepository {

    private static final JdbcOrderRepository INSTANCE = new JdbcOrderRepository();

    public static JdbcOrderRepository getInstance() {
        return INSTANCE;
    }


    @Override
    public void add(Order Order) {

    }

    @Override
    public Optional<Order> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Order> getByUserId(Long id) {
        return null;
    }

    @Override
    public void update(Order Order) {

    }

    @Override
    public void delete(Order Order) {

    }
}
