package ru.cft.focusstart.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Objects;

@JsonDeserialize(builder = OrderItemDto.Builder.class)
public class OrderItemDto extends AbstractEntityDto{

    private Long value;

    private Long productId;

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private Long id;

        private Long value;

        private Long productId;

        private Builder() {
        }

        private Builder(OrderItemDto orderItemDto) {
            this.id = orderItemDto.getId();
            this.value = orderItemDto.value;
            this.productId = orderItemDto.productId;
        }

        public OrderItemDto.Builder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderItemDto.Builder value(Long value) {
            this.value = value;
            return this;
        }

        public OrderItemDto.Builder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public OrderItemDto build() {
            return new OrderItemDto(this.id, this.value, this.productId);
        }
    }

    public OrderItemDto(Long id, Long value, Long productId) {
        super(id);
        this.value = value;
        this.productId = productId;
    }

    public static OrderItemDto.Builder builder() {
        return new OrderItemDto.Builder();
    }

    public OrderItemDto.Builder toBuilder() {
        return new OrderItemDto.Builder(this);
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemDto orderItemDto = (OrderItemDto) o;
        return Objects.equals(this.getId(), orderItemDto.getId()) &&
                Objects.equals(value, orderItemDto.value) &&
                Objects.equals(productId, orderItemDto.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), value, productId);
    }

    @Override
    public String toString() {
        return "OrderItemDto{" +
                "id=" + this.getId() +
                ", value=" + value +
                ", productId=" + productId +
                '}';
    }
}
