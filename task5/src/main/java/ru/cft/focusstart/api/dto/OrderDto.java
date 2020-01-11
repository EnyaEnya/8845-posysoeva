package ru.cft.focusstart.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonDeserialize(builder = OrderDto.Builder.class)
public class OrderDto {

    private Long id;

    private List<OrderEntityDto> orderEntities;

    private Long customerId;

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private Long id;

        private List<OrderEntityDto> orderEntities;

        private Long customerId;

        private Builder() {
        }

        private Builder(OrderDto orderDto) {
            this.id = orderDto.id;
            this.orderEntities = orderDto.orderEntities;
            this.customerId = orderDto.customerId;
        }

        public OrderDto.Builder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderDto.Builder orderEntities(List<OrderEntityDto> orderEntities) {
            this.orderEntities = orderEntities;
            return this;
        }

        public OrderDto.Builder customerId(Long customerId) {
            this.customerId = customerId;
            return this;
        }

        public OrderDto build() {
            return new OrderDto(this.id, this.orderEntities, this.customerId);
        }
    }

    public OrderDto(Long customerId) {
        this.customerId = customerId;
        this.orderEntities = new ArrayList<>();
    }

    public OrderDto(Long id, List<OrderEntityDto> orderEntities, Long customerId) {
        this.id = id;
        this.orderEntities = orderEntities;
        this.customerId = customerId;
    }

    public static OrderDto.Builder builder() {
        return new OrderDto.Builder();
    }

    public OrderDto.Builder toBuilder() {
        return new OrderDto.Builder(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderEntityDto> getOrderEntities() {
        return orderEntities;
    }

    public void setOrderEntities(List<OrderEntityDto> orderEntities) {
        this.orderEntities = orderEntities;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto orderDto = (OrderDto) o;
        return Objects.equals(id, orderDto.id) &&
                Objects.equals(orderEntities, orderDto.orderEntities) &&
                Objects.equals(customerId, orderDto.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderEntities, customerId);
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "id=" + id +
                ", orderEntities=" + orderEntities +
                ", customerId=" + customerId +
                '}';
    }
}
