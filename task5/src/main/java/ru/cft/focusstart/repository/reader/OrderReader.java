package ru.cft.focusstart.repository.reader;

import ru.cft.focusstart.entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class OrderReader {

    private OrderReader() {
    }

    public static Order readOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("order_id"));
        order.setCustomerId(rs.getLong("customer_id"));

        return order;
    }
}
