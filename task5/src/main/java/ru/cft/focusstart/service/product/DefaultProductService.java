package ru.cft.focusstart.service.product;

import ru.cft.focusstart.api.dto.ProductDto;
import ru.cft.focusstart.entity.Product;
import ru.cft.focusstart.mapper.ProductMapper;
import ru.cft.focusstart.repository.product.JdbcProductRepository;
import ru.cft.focusstart.repository.product.ProductRepository;
import ru.cft.focusstart.service.AbstractService;

import java.util.List;
import java.util.stream.Collectors;

import static ru.cft.focusstart.service.validation.Validator.*;

public class DefaultProductService extends AbstractService<Product, ProductDto, ProductRepository, ProductMapper> implements ProductService {

    private static final DefaultProductService INSTANCE = new DefaultProductService();

    private final ProductRepository productRepository = JdbcProductRepository.getInstance();

    private final ProductMapper productMapper = ProductMapper.getInstance();

    private DefaultProductService() {
    }

    public static ProductService getInstance() {
        return INSTANCE;
    }

    @Override
    public ProductMapper getMapper() {
        return productMapper;
    }

    @Override
    public ProductDto create(ProductDto productDto) {
        validate(productDto);

        Product product = add(null, productDto);

        return productMapper.toDto(product);
    }

    @Override
    public List<ProductDto> get(String title) {
        return productRepository.get(title)
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto merge(Long id, ProductDto productDto) {
        checkNotNull("id", id);
        validate(productDto);

        Product product = productRepository.getById(id)
                .map(existing -> update(existing, productDto))
                .orElseGet(() -> add(id, productDto));

        return productMapper.toDto(product);
    }

    @Override
    protected ProductRepository getRepository() {
        return productRepository;
    }

    private void validate(ProductDto productDto) {
        checkNull("product.id", productDto.getId());
        checkSize("product.title", productDto.getTitle(), 1, 256);
        checkSize("product.description", productDto.getDescription(), 1, 4096);
        checkSize("product.price", Long.toString(productDto.getPrice()), 1, 10);
    }

    private Product add(Long id, ProductDto productDto) {

        Product product;

        if (id != null) {
            product = getEntity(id);
        } else {
            product = new Product();
        }
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());

        productRepository.add(product);
        return product;
    }


    private Product update(Product product, ProductDto productDto) {
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());

        productRepository.update(product);

        return product;
    }
}
