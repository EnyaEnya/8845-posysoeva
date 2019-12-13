package ru.cft.focusstart.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cft.focusstart.dto.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;
import java.util.HashSet;

public class ClientService {

    private static ClientService INSTANCE;

    private static final Logger log = LoggerFactory.getLogger(ClientService.class);

    private ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private Socket socket;

    private String user;

    private BufferedReader reader;

    private PrintWriter writer;

    private Collection<String> users;

    private ClientService() {
        this.users = new HashSet<>();
    }

    public static synchronized ClientService getInstance () {
        if (INSTANCE == null) {
            INSTANCE = new ClientService();
        }
        return INSTANCE;
    }

    public void connectUser(ConnectData connectData) throws IOException {
        String ipAddress = connectData.getIpAddress();
        int port = connectData.getPort();
        String user = connectData.getUser();

        socket = new Socket(ipAddress, port);

        Thread thread = new Thread(new ClientConnection(socket));
        thread.setDaemon(true);
        thread.start();

        ConnectUser connectUser = new ConnectUser();
        connectUser.setUser(user);

        String jsonString = mapper.writeValueAsString(connectUser) + "\n";

        writer = new PrintWriter(socket.getOutputStream());

        writer.write(jsonString);
        writer.flush();
    }

    public void sendMessage(String message) throws JsonProcessingException {

        SendMessageFromUser sendMessageFromUser = new SendMessageFromUser();
        sendMessageFromUser.setMessage(message);

        String jsonString = mapper.writeValueAsString(sendMessageFromUser) + "\n";

        writer.write(jsonString);
        writer.flush();
    }

    public String showNewMessage(SendMessageFromServer messageFromServer) throws JsonProcessingException {
        return messageFromServer.getTime() + " <" + messageFromServer.getUser() + ">: "
                + messageFromServer.getMessage() + System.lineSeparator();
    };

    public String showAuthorizationMessage(AddUser addUser) throws JsonProcessingException {
        return addUser.getUser() + " joined to our chat!" + System.lineSeparator();
    };

    public String showRemoveUserMessage(RemoveUser removeUser) throws JsonProcessingException {
        return removeUser.getUser() + " left our chat" + System.lineSeparator();
    };

    public void makeHeartbeat() throws JsonProcessingException {
        Heartbeat heartbeat = new Heartbeat();
        String jsonString = mapper.writeValueAsString(heartbeat);
        writer.write(jsonString + "\n");
        writer.flush();
    }

    public void doService(String jsonString) throws JsonProcessingException {
        try {
            Method readValue = mapper.readValue(jsonString, Method.class);

            if (readValue instanceof AddUser) {
                Client.usersListBox.setText("");
                users.add(((AddUser) readValue).getUser());
                Client.chatBox.append(showAuthorizationMessage((AddUser) readValue));
                Client.usersListBox.append(formatUsersList());
            } else if (readValue instanceof SendMessageFromServer) {
                Client.chatBox.append(showNewMessage((SendMessageFromServer) readValue));
            } else if (readValue instanceof RemoveUser) {
                Client.usersListBox.setText("");
                users.remove(((RemoveUser) readValue).getUser());
                Client.chatBox.append(showRemoveUserMessage((RemoveUser) readValue));
                Client.usersListBox.append(formatUsersList());
            } else if (readValue instanceof Response) {
                updateUsersList((Response) readValue);
                Client.usersListBox.setText("");
                Client.usersListBox.append(formatUsersList());
            } else {
                log.error("invalid type: {}", readValue);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void updateUsersList(Response response) {
        users = response.getUsersList();
    }

    private String formatUsersList() {
        StringBuilder newList = new StringBuilder();
        users.forEach(element -> newList.append(element).append(System.lineSeparator()));
        return newList.toString();
    }

}
