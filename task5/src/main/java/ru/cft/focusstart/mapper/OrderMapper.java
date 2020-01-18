package ru.cft.focusstart.mapper;

import ru.cft.focusstart.api.dto.OrderDto;
import ru.cft.focusstart.api.dto.OrderItemDto;
import ru.cft.focusstart.entity.Order;
import ru.cft.focusstart.entity.OrderItem;

import java.util.stream.Collectors;

public class OrderMapper extends AbstractEntityMapper<Order, OrderDto> {

    private static final OrderMapper INSTANCE = new OrderMapper();

    private OrderMapper() {
    }

    public static OrderMapper getInstance() {
        return INSTANCE;
    }

    public OrderDto toDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .orderEntities(order.getOrderEntities().stream().map(this::toDto).collect(Collectors.toList()))
                .customerId(order.getCustomerId())
                .build();
    }

    private OrderItemDto toDto(OrderItem orderItem) {
        return OrderItemDto.builder()
                .id(orderItem.getId())
                .value(orderItem.getValue())
                .productId(orderItem.getProductId())
                .build();
    }

}
