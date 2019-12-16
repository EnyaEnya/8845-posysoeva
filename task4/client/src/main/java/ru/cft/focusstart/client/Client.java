package ru.cft.focusstart.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Client {

    public static final Client INSTANCE = new Client();

    private static final Logger log = LoggerFactory.getLogger(Client.class);
    private static final int ENTER_BUTTON = 13;
    private JTextArea chatBox;
    private JTextArea usersListBox;
    private JFrame frame = new JFrame("Simple chat");
    private ConnectData connectData = new ConnectData();
    private JTextField messageBox;
    private JTextField usernameChooser;
    private JTextField ipAddressChooser;
    private JTextField portChooser;
    private JFrame authorizationFrame;
    private JFrame errorFrame;

    public static void main(String[] args) {
        log.info("client started");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Client.INSTANCE.showLoginScreen();
            }
        });
    }

    private void showLoginScreen() {
        frame.setVisible(false);
        authorizationFrame = new JFrame("Simple chat");

        usernameChooser = new JTextField(40);
        ipAddressChooser = new JTextField(40);
        portChooser = new JTextField(40);

        JLabel chooseUsernameLabel = new JLabel("Enter username:");
        JLabel chooseIpAddressLabel = new JLabel("Enter server address:");
        JLabel choosePortLabel = new JLabel("Enter port:");

        JButton enterServer = new JButton("Connect Chat Server");

        JPanel authorizationPanel = new JPanel(new MigLayout());


        authorizationPanel.add(chooseUsernameLabel, "gap, sg 1");
        authorizationPanel.add(usernameChooser, "wrap");

        authorizationPanel.add(chooseIpAddressLabel, "gap, sg 1");
        authorizationPanel.add(ipAddressChooser, "wrap");
        ipAddressChooser.setText("localhost");

        authorizationPanel.add(choosePortLabel, "gap, sg 1");
        authorizationPanel.add(portChooser, "wrap");
        portChooser.setText("3443");


        authorizationFrame.add(BorderLayout.CENTER, authorizationPanel);
        authorizationFrame.add(BorderLayout.SOUTH, enterServer);
        authorizationFrame.setVisible(true);
        authorizationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        authorizationFrame.setSize(400, 300);

        enterServer.addActionListener(new ServerButtonListener());
    }

    public void showChatScreen() {
        frame.setVisible(true);

        JPanel southPanel = new JPanel();
        JPanel eastPanel = new JPanel();
        frame.add(BorderLayout.SOUTH, southPanel);
        frame.add(BorderLayout.EAST, eastPanel);

        messageBox = new JTextField(80);
        southPanel.add(messageBox);

        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setLineWrap(true);
        frame.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        usersListBox = new JTextArea(60, 15);
        usersListBox.setEditable(false);
        usersListBox.setLineWrap(true);

        eastPanel.add(usersListBox);

        JButton sendMessage = new JButton("Send Message");
        sendMessage.setMnemonic(ENTER_BUTTON);
        southPanel.add(sendMessage);

        sendMessage.addActionListener(new MessageButtonListener());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);
    }

    public void showErrorScreen(String message) {
        frame.setVisible(false);

        errorFrame = new JFrame("Error!");

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new onCloseErrorScreenListener());

        JLabel errorText = new JLabel(message);

        errorFrame.setVisible(true);
        errorFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        errorFrame.setSize(400, 300);
        errorFrame.add(errorText, BorderLayout.CENTER);
        errorFrame.add(okButton, BorderLayout.SOUTH);
    }

    public void addUser(String newUser) {
        usersListBox.setText("");
        chatBox.append(newUser + " joined to our chat!" + System.lineSeparator());
        usersListBox.append(ClientService.INSTANCE.formatUsersList());
    }

    public void showNewMessage(String time, String user, String message) {
        String resultMessage = time + " <" + user + ">: " + message + System.lineSeparator();
        chatBox.append(resultMessage);
    }

    public void removeUser(String removedUser) {
        usersListBox.setText("");
        chatBox.append(removedUser + " left our chat" + System.lineSeparator());
        usersListBox.append(ClientService.INSTANCE.formatUsersList());
    }

    public void updateUsersList() {
        usersListBox.setText("");
        usersListBox.append(ClientService.INSTANCE.formatUsersList());
    }

    class MessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (StringUtils.isBlank(messageBox.getText())) {
                // do nothing
            } else {
                String message = messageBox.getText();
                try {
                    ClientService.INSTANCE.sendMessage(message);
                    messageBox.setText("");
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class ServerButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            String user = usernameChooser.getText();
            String ipAddress = ipAddressChooser.getText();
            int port = Integer.parseInt(portChooser.getText());

            connectData.setUser(user);
            connectData.setIpAddress(ipAddress);
            connectData.setPort(port);

            if (user.length() < 1) {
                authorizationFrame.setVisible(false);
                showErrorScreen("Invalid username");
            } else {
                authorizationFrame.setVisible(false);
                try {
                    ClientService.INSTANCE.connectUser(connectData);
                    showChatScreen();
                } catch (IOException e) {
                    showErrorScreen(e.getMessage());
                }
            }
        }
    }

    class onCloseErrorScreenListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            showLoginScreen();
            errorFrame.setVisible(false);
        }
    }
}
