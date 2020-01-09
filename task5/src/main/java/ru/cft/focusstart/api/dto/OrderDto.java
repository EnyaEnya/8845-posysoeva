package ru.cft.focusstart.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.List;
import java.util.Objects;

@JsonDeserialize(builder = OrderDto.Builder.class)
public class OrderDto {

    private Long id;

    private List<OrderEntityDto> orderEntities;

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private Long id;

        private List<OrderEntityDto> orderEntities;

        private Builder() {
        }

        private Builder(OrderDto orderDto) {
            this.id = orderDto.id;
            this.orderEntities = orderDto.orderEntities;
        }

        public OrderDto.Builder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderDto.Builder orderEntities(List<OrderEntityDto> orderEntities) {
            this.orderEntities = orderEntities;
            return this;
        }

        public OrderDto build() {
            return new OrderDto(this.id, this.orderEntities);
        }
    }

    public OrderDto(Long id, List<OrderEntityDto> orderEntities) {
        this.id = id;
        this.orderEntities = orderEntities;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto orderDto = (OrderDto) o;
        return Objects.equals(id, orderDto.id) &&
                Objects.equals(orderEntities, orderDto.orderEntities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderEntities);
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "id=" + id +
                ", orderEntities=" + orderEntities +
                '}';
    }
}
