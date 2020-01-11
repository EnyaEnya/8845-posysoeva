package ru.cft.focusstart.service.order;

import ru.cft.focusstart.api.dto.OrderDto;
import ru.cft.focusstart.entity.Order;
import ru.cft.focusstart.entity.OrderEntity;
import ru.cft.focusstart.exception.ObjectNotFoundException;
import ru.cft.focusstart.mapper.OrderMapper;
import ru.cft.focusstart.repository.order.JdbcOrderRepository;
import ru.cft.focusstart.repository.order.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.cft.focusstart.service.validation.Validator.checkNotNull;

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
        OrderDto orderDto = new OrderDto(customerId);
        validate(orderDto);

        Order order = add(orderDto);

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto getById(Long customerId, Long id) {
        checkNotNull("id", id);

        Order order = getOrder(id);

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
        checkNotNull("id", id);

        Order order = getOrder(id);

        orderRepository.delete(order);
    }

    private void validate(OrderDto orderDto) {
        checkNotNull("order.customerId", orderDto.getCustomerId());
    }

    private Order add(OrderDto orderDto) {

        Order order = new Order();
        order.setCustomerId(orderDto.getCustomerId());

        order.setOrderEntities(orderDto.getOrderEntities().stream().map(orderEntityDto -> {
            OrderEntity orderEntity = new OrderEntity();

            orderEntity.setProductId(orderEntityDto.getProductId());
            orderEntity.setValue(orderEntityDto.getValue());
            return orderEntity;
        }).collect(Collectors.toList()));

        orderRepository.add(order);

        return order;
    }



    private Order getOrder(Long id) {
        return orderRepository.getById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Order with id %s not found", id)));
    }

    private Order update(Order order, OrderDto orderDto) {
        order.setCustomerId(orderDto.getCustomerId());

        order.setOrderEntities(orderDto.getOrderEntities().stream().map(orderEntityDto -> {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setId(orderEntityDto.getId());
            orderEntity.setOrderId(order.getId());
            orderEntity.setProductId(orderEntityDto.getProductId());
            orderEntity.setValue(orderEntityDto.getValue());
            return orderEntity;
        }).collect(Collectors.toList()));

        order.getOrderEntities().forEach(entity -> {
            if (entity.getId() == null) {
                orderRepository.insertOrderEntity(entity);
            } else {
                orderRepository.updateOrderEntity(entity);
            }
        });

        orderRepository.update(order);

        return order;
    }

}
