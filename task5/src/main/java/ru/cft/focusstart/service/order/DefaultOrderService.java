package ru.cft.focusstart.service.order;

import ru.cft.focusstart.api.dto.OrderDto;
import ru.cft.focusstart.mapper.OrderMapper;
import ru.cft.focusstart.repository.order.JdbcOrderRepository;
import ru.cft.focusstart.repository.order.OrderRepository;

import java.util.List;

public class DefaultOrderService implements OrderService {

    private static final DefaultOrderService INSTANCE = new DefaultOrderService();

    private final OrderRepository orderRepository = JdbcOrderRepository.getInstance();

    private final OrderMapper orderMapper = OrderMapper.getInstance();

    private DefaultOrderService() {
    }

    public static OrderService getInstance() {
        return INSTANCE;
    }

    @Override
    public OrderDto create(Long customerId) {
        return null;
    }

    @Override
    public OrderDto getById(Long customerId, Long id) {
        return null;
    }

    @Override
    public List<OrderDto> getByCustomerId(Long customerId) {
        return null;
    }

    @Override
    public OrderDto merge(Long id, OrderDto orderDto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
