package ru.cft.focusstart.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.cft.focusstart.api.dto.ProductDto;
import ru.cft.focusstart.service.product.DefaultProductService;
import ru.cft.focusstart.service.product.ProductService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ru.cft.focusstart.api.PathParser.getPathPart;

public class ProductServlet extends HttpServlet {

    private static final String PRODUCTS_PATTERN = "^/products";
    private static final String PRODUCT_PATTERN = "^/products/(?<id>[0-9]+)$";

    private final ObjectMapper mapper = new ObjectMapper();

    private final ProductService productService = DefaultProductService.getInstance();

    private final ExceptionHandler exceptionHandler = ExceptionHandler.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = getPath(req);
            if (path.matches(PRODUCTS_PATTERN)) {
                getAllProducts(req, resp);
            } else if (path.matches(PRODUCT_PATTERN)) {
                getProductById(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionHandler.handleExceptions(e, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = getPath(req);
            if (path.matches(PRODUCTS_PATTERN)) {
                createProduct(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionHandler.handleExceptions(e, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = getPath(req);
            if (path.matches(PRODUCT_PATTERN)) {
                updateProduct(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionHandler.handleExceptions(e, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = getPath(req);
            if (path.matches(PRODUCT_PATTERN)) {
                deleteProduct(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionHandler.handleExceptions(e, resp);
        }
    }

    private void createProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ProductDto request = mapper.readValue(req.getInputStream(), ProductDto.class);

        ProductDto response = productService.create(request);
        writeResp(resp, response);
    }

    private void getProductById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = getPathPart(getPath(req), PRODUCT_PATTERN, "id");

        ProductDto response = productService.getById(id);
        writeResp(resp, response);
    }

    private void getAllProducts(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String title = req.getParameter("title");

        List<ProductDto> response = productService.get(title);
        writeResp(resp, response);
    }

    private void updateProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = getPathPart(getPath(req), PRODUCT_PATTERN, "id");
        ProductDto request = mapper.readValue(req.getInputStream(), ProductDto.class);

        ProductDto response = productService.merge(id, request);
        writeResp(resp, response);
    }

    private void deleteProduct(HttpServletRequest req, HttpServletResponse resp) {
        Long id = getPathPart(getPath(req), PRODUCT_PATTERN, "id");

        productService.delete(id);
    }

    private String getPath(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }

    private void writeResp(HttpServletResponse resp, Object response) throws IOException {
        resp.setContentType("application/json");
        mapper.writeValue(resp.getOutputStream(), response);
    }
}
