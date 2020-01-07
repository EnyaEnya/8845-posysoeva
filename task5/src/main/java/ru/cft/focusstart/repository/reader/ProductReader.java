package ru.cft.focusstart.repository.reader;

import ru.cft.focusstart.entity.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ProductReader {

    private ProductReader() {
    }

    public static Product readProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getLong("product_id"));
        product.setTitle(rs.getString("product_title"));
        product.setDescription(rs.getString("product_description"));
        product.setPrice(rs.getLong("product_price"));
        return product;
    }
}
