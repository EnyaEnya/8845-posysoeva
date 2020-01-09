package ru.cft.focusstart.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.cft.focusstart.api.dto.CustomerDto;
import ru.cft.focusstart.api.dto.OrderDto;
import ru.cft.focusstart.service.customer.CustomerService;
import ru.cft.focusstart.service.customer.DefaultCustomerService;
import ru.cft.focusstart.service.order.DefaultOrderService;
import ru.cft.focusstart.service.order.OrderService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ru.cft.focusstart.api.PathParser.getPathPart;

public class CustomerServlet extends HttpServlet {

    private static final String CUSTOMERS_PATTERN = "^/customers";
    private static final String CUSTOMER_PATTERN = "^/customers/(?<customerId>[0-9]+)$";
    private static final String ORDERS_PATTERN = "^/customers/(?<customerId>[0-9]+)/order$";
    private static final String ORDER_PATTERN = "^/customers/(?<customerId>[0-9]+)/order/(?<orderId>[0-9]+)$";

    private final ObjectMapper mapper = new ObjectMapper();

    private final CustomerService customerService = DefaultCustomerService.getInstance();

    private final OrderService orderService = DefaultOrderService.getInstance();

    private final ExceptionHandler exceptionHandler = ExceptionHandler.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = getPath(req);
            if (path.matches(ORDERS_PATTERN)) {
                getOrdersById(req, resp);
            } else if (path.matches(ORDER_PATTERN)) {
                getOrderById(req, resp);
            } else if (path.matches(CUSTOMERS_PATTERN)) {
                get(req, resp);
            } else if (path.matches(CUSTOMER_PATTERN)) {
                getById(req, resp);
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
            if (path.matches(ORDERS_PATTERN)) {
                createOrder(req, resp);
            } else if (path.matches(CUSTOMERS_PATTERN)) {
                create(req, resp);
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
            if (path.matches(ORDER_PATTERN)) {

            } else if (path.matches(CUSTOMER_PATTERN)) {
                merge(req, resp);
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
            if (path.matches(CUSTOMER_PATTERN)) {
                delete(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionHandler.handleExceptions(e, resp);
        }
    }

    private void create(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CustomerDto request = mapper.readValue(req.getInputStream(), CustomerDto.class);

        CustomerDto response = customerService.create(request);
        writeResp(resp, response);
    }

    private void createOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long customerId = getPathPart(getPath(req), ORDERS_PATTERN, "customerId");

        OrderDto response = orderService.create(customerId);
        writeResp(resp, response);
    }

    private void getById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long customerId = getPathPart(getPath(req), CUSTOMER_PATTERN, "customerId");

        CustomerDto response = customerService.getById(customerId);
        writeResp(resp, response);
    }

    private void getOrdersById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long customerId = getPathPart(getPath(req), ORDERS_PATTERN, "customerId");

        List<OrderDto> response = orderService.getByCustomerId(customerId);
        writeResp(resp, response);
    }

    private void getOrderById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = getPath(req);
        Long customerId = getPathPart(path, ORDER_PATTERN, "customerId");
        Long orderId = getPathPart(path, ORDER_PATTERN, "orderId");

        OrderDto response = orderService.getById(customerId, orderId);
        writeResp(resp, response);
    }

    private void get(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");

        List<CustomerDto> response = customerService.get(firstName, lastName);
        writeResp(resp, response);
    }

    private void merge(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long customerId = getPathPart(getPath(req), CUSTOMER_PATTERN, "customerId");
        CustomerDto request = mapper.readValue(req.getInputStream(), CustomerDto.class);

        CustomerDto response = customerService.merge(customerId, request);
        writeResp(resp, response);
    }

    private void mergeOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long customerId = getPathPart(getPath(req), ORDER_PATTERN, "customerId");
        Long orderId = getPathPart(getPath(req), ORDER_PATTERN, "orderId");


    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long customerId = getPathPart(getPath(req), CUSTOMER_PATTERN, "customerId");

        customerService.delete(customerId);
    }

    private String getPath(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }

    private void writeResp(HttpServletResponse resp, Object response) throws IOException {
        resp.setContentType("application/json");
        mapper.writeValue(resp.getOutputStream(), response);
    }
}
