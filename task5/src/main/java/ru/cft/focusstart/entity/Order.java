package ru.cft.focusstart.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order extends AbstractEntity {

    private Long customerId;

    private List<OrderItem> orderEntities;

    public Order() {
        this.orderEntities = new ArrayList<>();
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<OrderItem> getOrderEntities() {
        return orderEntities;
    }

    public void setOrderEntities(List<OrderItem> orderEntities) {
        this.orderEntities = orderEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(customerId, order.customerId) &&
                Objects.equals(orderEntities, order.orderEntities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, orderEntities);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", orderEntities=" + orderEntities +
                '}';
    }
}
