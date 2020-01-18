package ru.cft.focusstart.repository.reader;

import ru.cft.focusstart.entity.OrderItem;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class OrderItemReader {

    private OrderItemReader() {
    }

    public static OrderItem readOrderItem(ResultSet rs) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(rs.getLong("entity_id"));
        orderItem.setProductId(rs.getLong("product_id"));
        orderItem.setValue(rs.getLong("product_value"));

        return orderItem;
    }
}
