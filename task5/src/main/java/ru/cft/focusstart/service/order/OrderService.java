package ru.cft.focusstart.service.order;

import ru.cft.focusstart.api.dto.OrderDto;
import ru.cft.focusstart.service.EntityService;

import java.util.List;

public interface OrderService extends EntityService<OrderDto> {

    OrderDto create(Long customerId);

    List<OrderDto> getByCustomerId(Long customerId);

    OrderDto merge(OrderDto orderDto);

}
