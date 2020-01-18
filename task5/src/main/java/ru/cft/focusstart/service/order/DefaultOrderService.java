package ru.cft.focusstart.service.order;

import ru.cft.focusstart.api.dto.OrderDto;
import ru.cft.focusstart.entity.Order;
import ru.cft.focusstart.entity.OrderItem;
import ru.cft.focusstart.mapper.OrderMapper;
import ru.cft.focusstart.repository.order.JdbcOrderRepository;
import ru.cft.focusstart.repository.order.OrderRepository;
import ru.cft.focusstart.repository.orderitem.JdbcOrderItemRepository;
import ru.cft.focusstart.repository.orderitem.OrderItemRepository;
import ru.cft.focusstart.service.AbstractService;

import java.util.List;
import java.util.stream.Collectors;

import static ru.cft.focusstart.service.validation.Validator.checkNotNull;

public class DefaultOrderService extends AbstractService<Order, OrderDto, OrderRepository, OrderMapper> implements OrderService {

    private static final DefaultOrderService INSTANCE = new DefaultOrderService();

    private final OrderRepository orderRepository = JdbcOrderRepository.getInstance();

    private final OrderItemRepository orderItemRepository = JdbcOrderItemRepository.getInstance();

    private final OrderMapper orderMapper = OrderMapper.getInstance();

    private DefaultOrderService() {
    }

    public static OrderService getInstance() {
        return INSTANCE;
    }

    @Override
    public OrderMapper getMapper() {
        return orderMapper;
    }

    @Override
    public OrderDto create(Long customerId) {
        OrderDto orderDto = new OrderDto(customerId);
        validate(orderDto);

        Order order = add(orderDto);

        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> getByCustomerId(Long customerId) {
        checkNotNull("customerId", customerId);
        return orderRepository.getByCustomerId(customerId).stream().map(orderMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public OrderDto merge(OrderDto orderDto) {
        validate(orderDto);

        Order order = orderRepository.getById(orderDto.getId())
                .map(existing -> update(existing, orderDto))
                .orElseGet(() -> add(orderDto));

        return orderMapper.toDto(order);
    }

    @Override
    public void delete(Long id) {
        orderItemRepository.delete(id);
        super.delete(id);
    }

    @Override
    protected OrderRepository getRepository() {
        return orderRepository;
    }

    private void validate(OrderDto orderDto) {
        checkNotNull("order.customerId", orderDto.getCustomerId());
    }

    private Order add(OrderDto orderDto) {

        Order order = new Order();
        order.setCustomerId(orderDto.getCustomerId());

        order.setOrderEntities(orderDto.getOrderEntities().stream().map(orderEntityDto -> {
            OrderItem orderItem = new OrderItem();

            orderItem.setProductId(orderEntityDto.getProductId());
            orderItem.setValue(orderEntityDto.getValue());
            return orderItem;
        }).collect(Collectors.toList()));

        orderRepository.add(order);

        return order;
    }

    private Order update(Order order, OrderDto orderDto) {
        order.setCustomerId(orderDto.getCustomerId());

        order.setOrderEntities(orderDto.getOrderEntities().stream().map(orderEntityDto -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(orderEntityDto.getId());
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(orderEntityDto.getProductId());
            orderItem.setValue(orderEntityDto.getValue());
            return orderItem;
        }).collect(Collectors.toList()));

        order.getOrderEntities().forEach(entity -> {
            if (entity.getId() == null) {
                orderItemRepository.add(entity);
            } else {
                orderItemRepository.update(entity);
            }
        });

        orderRepository.update(order);

        return order;
    }

}
