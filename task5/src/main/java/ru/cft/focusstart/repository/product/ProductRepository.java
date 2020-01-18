package ru.cft.focusstart.repository.product;

import ru.cft.focusstart.entity.Product;
import ru.cft.focusstart.repository.EntityRepository;

import java.util.List;

public interface ProductRepository extends EntityRepository<Product> {

    List<Product> get(String title);

}
