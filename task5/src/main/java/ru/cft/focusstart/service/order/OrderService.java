package ru.cft.focusstart.service.order;

import ru.cft.focusstart.api.dto.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto create(Long customerId);

    OrderDto getById(Long customerId, Long id);

    List<OrderDto> getByCustomerId(Long customerId);

    OrderDto merge(OrderDto orderDto);

    void delete(Long id);

}
