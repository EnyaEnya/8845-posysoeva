package ru.cft.focusstart.mapper;

import ru.cft.focusstart.api.dto.OrderDto;
import ru.cft.focusstart.api.dto.OrderEntityDto;
import ru.cft.focusstart.entity.Order;
import ru.cft.focusstart.entity.OrderEntity;

import java.util.stream.Collectors;

public class OrderMapper {

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
                .build();
    }

    private OrderEntityDto toDto(OrderEntity orderEntity) {
        return OrderEntityDto.builder()
                .id(orderEntity.getId())
                .value(orderEntity.getValue())
                .productId(orderEntity.getProductId())
                .build();
    }

}
