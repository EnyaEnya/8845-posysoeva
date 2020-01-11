package ru.cft.focusstart.repository.reader;

import ru.cft.focusstart.entity.OrderEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class OrderEntityReader {

    private OrderEntityReader() {
    }

    public static OrderEntity readOrderEntity(ResultSet rs) throws SQLException {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(rs.getLong("entity_id"));
        orderEntity.setProductId(rs.getLong("product_id"));
        orderEntity.setValue(rs.getLong("product_value"));

        return orderEntity;
    }
}
