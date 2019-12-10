package ru.cft.focusstart.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cft.focusstart.dto.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;


public class ConnectionService {

    private static final Logger log = LoggerFactory.getLogger(ConnectionService.class);

    public static final ConnectionService INSTANCE = new ConnectionService();

    private static final long MAX_IDLE_TIME = TimeUnit.SECONDS.toNanos(60);

    private ConcurrentMap<String, Connection> usersStore = new ConcurrentHashMap<String, Connection>();

    private ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public void doService(String jsonString, Connection connection) throws JsonProcessingException {

        Response response;

        try {

            Method readValue = mapper.readValue(jsonString, Method.class);

            if (readValue instanceof Heartbeat) {
                //do nothing
            } else if (readValue instanceof ConnectUser) {
                response = connectUser((ConnectUser) readValue, connection);
                write(response, connection);
            } else if (readValue instanceof GetUsersList) {
                response = getUsersList();
                write(response, connection);
            } else if (readValue instanceof SendMessageFromUser) {
                sendMessageFromUser((SendMessageFromUser) readValue, connection);
            } else {
                response = new Response(Status.ERROR);
                write(response, connection);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            response = new Response(Status.ERROR);
            write(response, connection);
        }
    }

    public void checkoutConnections() {
        List<String> interruptedConnections = new ArrayList<String>();
        usersStore.forEach((k, v) -> {
            if (v.getLastEventTime() < System.nanoTime() - MAX_IDLE_TIME) {
                interruptedConnections.add(k);
            }
        });
        if (interruptedConnections.size() > 0) {
            interruptedConnections.forEach(k -> {
                try {
                    Connection connection = usersStore.get(k);
                    usersStore.remove(k);
                    log.info("user {} removed", connection);
                    removeUser(connection);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private Response connectUser(ConnectUser connectUser, Connection connection) throws JsonProcessingException {
        String user = connectUser.getUser();
        Response response = new Response();
        if (!usersStore.containsKey(user)) {
            connection.setUser(user);
            sendAll(new AddUser(user));
            usersStore.put(user, connection);
            response.setStatus(Status.OK);
        } else {
            response.setStatus(Status.ERROR);
            response.setErrorText("This user already exists");
        }
        return response;
    }

    private void removeUser(Connection connection) throws IOException {
        RemoveUser removeUser = new RemoveUser();
        removeUser.setUser(connection.getUser());
        connection.close();
        sendAll(removeUser);
    }

    private Response getUsersList() {
        return new UsersList(usersStore.keySet());
    }

    private void sendMessageFromUser(SendMessageFromUser sendMessageFromUser, Connection connection) throws JsonProcessingException {
        sendAll(new SendMessageFromServer(connection.getUser(), sendMessageFromUser.getMessage()));
    }

    private void sendAll(Object object) throws JsonProcessingException {
        String userMessage = mapper.writeValueAsString(object);
        usersStore.forEach((k, v) -> v.writeResponse(userMessage));
    }

    private void write(Response response, Connection connection) throws JsonProcessingException {
        connection.writeResponse(mapper.writeValueAsString(response));
    }
}
