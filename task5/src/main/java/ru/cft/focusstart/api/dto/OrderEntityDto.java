package ru.cft.focusstart.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Objects;

@JsonDeserialize(builder = OrderEntityDto.Builder.class)
public class OrderEntityDto {

    private Long id;

    private Long value;

    private Long productId;

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private Long id;

        private Long value;

        private Long productId;

        private Builder() {
        }

        private Builder(OrderEntityDto orderEntityDto) {
            this.id = orderEntityDto.id;
            this.value = orderEntityDto.value;
            this.productId = orderEntityDto.productId;
        }

        public OrderEntityDto.Builder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderEntityDto.Builder value(Long value) {
            this.value = value;
            return this;
        }

        public OrderEntityDto.Builder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public OrderEntityDto build() {
            return new OrderEntityDto(this.id, this.value, this.productId);
        }
    }

    public OrderEntityDto(Long id, Long value, Long productId) {
        this.id = id;
        this.value = value;
        this.productId = productId;
    }

    public static OrderEntityDto.Builder builder() {
        return new OrderEntityDto.Builder();
    }

    public OrderEntityDto.Builder toBuilder() {
        return new OrderEntityDto.Builder(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        OrderEntityDto that = (OrderEntityDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(value, that.value) &&
                Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, productId);
    }

    @Override
    public String toString() {
        return "OrderEntityDto{" +
                "id=" + id +
                ", value=" + value +
                ", productId=" + productId +
                '}';
    }
}
