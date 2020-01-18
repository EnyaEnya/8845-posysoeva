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
    private static final String ORDERS_PATTERN = "^/customers/(?<customerId>[0-9]+)/orders$";
    private static final String ORDER_PATTERN = "^/customers/(?<customerId>[0-9]+)/orders/(?<orderId>[0-9]+)$";
    private static final String PUT_ORDER_PATTERN = "^/customers/orders";

    private final ObjectMapper mapper = new ObjectMapper();

    private final CustomerService customerService = DefaultCustomerService.getInstance();

    private final OrderService orderService = DefaultOrderService.getInstance();

    private final ExceptionHandler exceptionHandler = ExceptionHandler.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = getPath(req);
            if (path.matches(CUSTOMERS_PATTERN)) {
                getCustomers(req, resp);
            } else if (path.matches(CUSTOMER_PATTERN)) {
                getCustomerById(req, resp);
            } else if (path.matches(ORDERS_PATTERN)) {
                getAllOrdersByCustomerId(req, resp);
            } else if (path.matches(ORDER_PATTERN)) {
                getOrderByCustomerId(req, resp);
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
            if (path.matches(CUSTOMERS_PATTERN)) {
                createCustomer(req, resp);
            } else if (path.matches(ORDERS_PATTERN)) {
                createOrder(req, resp);
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
            if (path.matches(CUSTOMER_PATTERN)) {
                updateCustomer(req, resp);
            } else if (path.matches(PUT_ORDER_PATTERN)) {
                updateOrder(req, resp);
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
                deleteCustomer(req, resp);
            } else if (path.matches(ORDER_PATTERN)) {
                deleteOrder(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionHandler.handleExceptions(e, resp);
        }
    }

    private void createCustomer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CustomerDto request = mapper.readValue(req.getInputStream(), CustomerDto.class);

        CustomerDto response = customerService.create(request);
        writeResp(resp, response);
    }

    private void createOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long customerId = getPathPart(getPath(req), ORDERS_PATTERN, "customerId");

        OrderDto response = orderService.create(customerId);
        writeResp(resp, response);
    }

    private void getCustomerById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long customerId = getPathPart(getPath(req), CUSTOMER_PATTERN, "customerId");

        CustomerDto response = customerService.getById(customerId);
        writeResp(resp, response);
    }

    private void getAllOrdersByCustomerId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long customerId = getPathPart(getPath(req), ORDERS_PATTERN, "customerId");

        List<OrderDto> response = orderService.getByCustomerId(customerId);
        writeResp(resp, response);
    }

    private void getOrderByCustomerId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = getPath(req);
        Long customerId = getPathPart(path, ORDER_PATTERN, "customerId");
        Long orderId = getPathPart(path, ORDER_PATTERN, "orderId");

        OrderDto response = orderService.getById(orderId);
        writeResp(resp, response);
    }

    private void getCustomers(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");

        List<CustomerDto> response = customerService.get(firstName, lastName);
        writeResp(resp, response);
    }

    private void updateCustomer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long customerId = getPathPart(getPath(req), CUSTOMER_PATTERN, "customerId");
        CustomerDto request = mapper.readValue(req.getInputStream(), CustomerDto.class);

        CustomerDto response = customerService.merge(customerId, request);
        writeResp(resp, response);
    }

    private void updateOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        OrderDto request = mapper.readValue(req.getInputStream(), OrderDto.class);

        OrderDto response = orderService.merge(request);
        writeResp(resp, response);
    }

    private void deleteCustomer(HttpServletRequest req, HttpServletResponse resp) {
        Long customerId = getPathPart(getPath(req), CUSTOMER_PATTERN, "customerId");

        customerService.delete(customerId);
    }


    private void deleteOrder(HttpServletRequest req, HttpServletResponse resp) {
        Long orderId = getPathPart(getPath(req), ORDER_PATTERN, "orderId");

        orderService.delete(orderId);
    }

    private String getPath(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }

    private void writeResp(HttpServletResponse resp, Object response) throws IOException {
        resp.setContentType("application/json");
        mapper.writeValue(resp.getOutputStream(), response);
    }
}
