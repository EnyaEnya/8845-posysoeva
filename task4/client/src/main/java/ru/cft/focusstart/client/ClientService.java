package ru.cft.focusstart.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cft.focusstart.dto.*;

import java.io.IOException;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;

public class ClientService {

    public static final ClientService INSTANCE = new ClientService();

    private static final Logger log = LoggerFactory.getLogger(ClientService.class);

    private ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private Collection<String> users;

    private ClientService() {
        this.users = new HashSet<>();
    }

    private ClientConnection clientConnection;

    public void connectUser(ConnectData connectData) throws IOException {
        String ipAddress = connectData.getIpAddress();
        int port = connectData.getPort();
        String user = connectData.getUser();

        Socket socket = new Socket(ipAddress, port);

        clientConnection = new ClientConnection(socket);

        Thread thread = new Thread(clientConnection);
        thread.setDaemon(true);
        thread.start();

        ConnectUser connectUser = new ConnectUser();
        connectUser.setUser(user);

        write(connectUser);
    }

    public void sendMessage(String message) throws JsonProcessingException {
        SendMessageFromUser sendMessageFromUser = new SendMessageFromUser();
        sendMessageFromUser.setMessage(message);
        write(sendMessageFromUser);
    }

    private void showNewMessage(SendMessageFromServer messageFromServer) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        Client.INSTANCE.showNewMessage(dtf.format(messageFromServer.getTime()), messageFromServer.getUser(),
                messageFromServer.getMessage());
    }

    private void addUser(AddUser addUser) {
        String newUser = addUser.getUser();
        users.add(newUser);
        Client.INSTANCE.addUser(newUser);
    }

    private void removeUser(RemoveUser removeUser) {
        String removedUser = removeUser.getUser();
        users.remove(removedUser);
        Client.INSTANCE.removeUser(removedUser);
    }

    public void makeHeartbeat() throws JsonProcessingException {
        Heartbeat heartbeat = new Heartbeat();
        write(heartbeat);
    }

    public void doService(String jsonString) {
        try {
            Method readValue = mapper.readValue(jsonString, Method.class);
            if (readValue instanceof AddUser) {
                addUser((AddUser) readValue);
            } else if (readValue instanceof SendMessageFromServer) {
                showNewMessage((SendMessageFromServer) readValue);
            } else if (readValue instanceof RemoveUser) {
                removeUser((RemoveUser) readValue);
            } else if (readValue instanceof Response) {
                checkResponseStatus((Response) readValue);
            } else {
                log.error("invalid type: {}", readValue);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void checkResponseStatus(Response response) {
        if (response.getStatus() != Status.ERROR) {
            users = response.getUsersList();
            Client.INSTANCE.updateUsersList();
        } else {
            Client.INSTANCE.showErrorScreen(response.getErrorText());
        }
    }

    public String formatUsersList() {
        StringBuilder newList = new StringBuilder();
        users.forEach(element -> newList.append(element).append(System.lineSeparator()));
        return newList.toString();
    }

    private void write(Method method) throws JsonProcessingException {
        clientConnection.write(mapper.writeValueAsString(method));
    }
}
