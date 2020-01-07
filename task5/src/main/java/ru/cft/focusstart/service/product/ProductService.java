package ru.cft.focusstart.service.product;

import ru.cft.focusstart.api.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto create(ProductDto productDto);

    ProductDto getById(Long id);

    List<ProductDto> get(String title);

    ProductDto merge(Long id, ProductDto productDto);

    void delete(Long id);
}
