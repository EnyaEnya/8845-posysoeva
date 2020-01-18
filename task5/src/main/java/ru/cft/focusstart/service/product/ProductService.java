package ru.cft.focusstart.service.product;

import ru.cft.focusstart.api.dto.ProductDto;
import ru.cft.focusstart.service.EntityService;

import java.util.List;

public interface ProductService extends EntityService<ProductDto> {

    ProductDto create(ProductDto productDto);

    List<ProductDto> get(String title);

    ProductDto merge(Long id, ProductDto productDto);

}
