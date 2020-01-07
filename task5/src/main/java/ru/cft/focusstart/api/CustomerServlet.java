package ru.cft.focusstart.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.cft.focusstart.api.dto.CustomerDto;
import ru.cft.focusstart.service.customer.CustomerService;
import ru.cft.focusstart.service.customer.DefaultCustomerService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ru.cft.focusstart.api.PathParser.getPathPart;

public class CustomerServlet extends HttpServlet {

    private static final String CUSTOMERS_PATTERN = "^/customers";
    private static final String CUSTOMER_PATTERN = "^/customers/(?<id>[0-9]+)$";

    private final ObjectMapper mapper = new ObjectMapper();

    private final CustomerService customerService = DefaultCustomerService.getInstance();

    private final ExceptionHandler exceptionHandler = ExceptionHandler.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = getPath(req);
            if (path.matches(CUSTOMERS_PATTERN)) {
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
            if (path.matches(CUSTOMERS_PATTERN)) {
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
            if (path.matches(CUSTOMER_PATTERN)) {
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

    private void getById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = getPathPart(getPath(req), CUSTOMER_PATTERN, "id");

        CustomerDto response = customerService.getById(id);
        writeResp(resp, response);
    }

    private void get(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");

        List<CustomerDto> response = customerService.get(firstName, lastName);
        writeResp(resp, response);
    }

    private void merge(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = getPathPart(getPath(req), CUSTOMER_PATTERN, "id");
        CustomerDto request = mapper.readValue(req.getInputStream(), CustomerDto.class);

        CustomerDto response = customerService.merge(id, request);
        writeResp(resp, response);
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = getPathPart(getPath(req), CUSTOMER_PATTERN, "id");

        customerService.delete(id);
    }

    private String getPath(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }

    private void writeResp(HttpServletResponse resp, Object response) throws IOException {
        resp.setContentType("application/json");
        mapper.writeValue(resp.getOutputStream(), response);
    }
}
