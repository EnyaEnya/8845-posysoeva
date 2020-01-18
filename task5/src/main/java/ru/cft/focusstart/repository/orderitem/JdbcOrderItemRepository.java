package ru.cft.focusstart.repository.orderitem;

import ru.cft.focusstart.entity.OrderItem;
import ru.cft.focusstart.repository.AbstractRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.cft.focusstart.repository.reader.OrderItemReader.readOrderItem;

public class JdbcOrderItemRepository extends AbstractRepository<OrderItem> implements OrderItemRepository{

    private static final JdbcOrderItemRepository INSTANCE = new JdbcOrderItemRepository();

    public static JdbcOrderItemRepository getInstance() {
        return INSTANCE;
    }

    private static final String GET_QUERY =
            "select c.id          entity_id," +
                    "       c.order_id        order_id," +
                    "       c.product_id product_id," +
                    "       c.value product_value," +
                    "from order_entity c " +
                    "where true";

    private static final String GET_BY_ID_QUERY =
            GET_QUERY +
                    " and c.id = ?";

    private static final String INSERT_ENTITIES_QUERY =
            "insert into order_entity (order_id, product_id, value) " +
                    "values (?, ?, ?)";

    private static final String UPDATE_ENTITIES_QUERY =
            "update order_entity " +
                    "set product_id = ?, value = ? " +
                    " where id = ?";

    private static final String DELETE_ENTITY_QUERY =
            "delete from order_entity where order_id = ?";

    @Override
    protected String getAddQuery() {
        return INSERT_ENTITIES_QUERY;
    }

    @Override
    protected String getByIdQuery() {
        return GET_BY_ID_QUERY;
    }

    @Override
    protected String getDeleteQuery() {
        return DELETE_ENTITY_QUERY;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_ENTITIES_QUERY;
    }

    @Override
    protected void prepareAddStatement(OrderItem orderItem, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, orderItem.getOrderId());
        preparedStatement.setLong(2, orderItem.getProductId());
        preparedStatement.setLong(3, orderItem.getValue());
    }

    @Override
    protected void prepareUpdateStatement(OrderItem orderItem, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, orderItem.getProductId());
        preparedStatement.setLong(2, orderItem.getValue());
        preparedStatement.setLong(3, orderItem.getId());
    }

    @Override
    protected Collection<OrderItem> readEntityList(ResultSet resultSet) throws SQLException {
        List<OrderItem> result = new ArrayList<>();
        while (resultSet.next()) {
            OrderItem orderItem = readOrderItem(resultSet);
            result.add(orderItem);
        }
        return result;

    }
}
