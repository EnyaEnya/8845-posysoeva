package ru.cft.focusstart.repository.product;

import ru.cft.focusstart.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    void add(Product product);

    Optional<Product> getById(Long id);

    List<Product> get(String title);

    void update(Product product);

    void delete(Product product);
}
