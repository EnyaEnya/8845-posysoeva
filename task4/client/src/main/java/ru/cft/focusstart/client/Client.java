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

    private static final Logger log = LoggerFactory.getLogger(Client.class);
    private static final int ENTER_BUTTON = 13;
    public static JTextArea chatBox;
    public static JTextArea usersListBox;
    private JFrame frame = new JFrame("Simple chat");
    private JTextField messageBox;
    private JTextField usernameChooser;
    private JTextField ipAddressChooser;
    private JTextField portChooser;
    private JFrame authorizationFrame;
    private JFrame errorFrame;

    public static void main(String[] args) {
        log.info("chat started");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Client clientGUI = new Client();
                clientGUI.showLoginScreen();
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
        authorizationFrame.setSize(300, 300);

        enterServer.addActionListener(new ServerButtonListener());
    }

    private void showChatScreen() {
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

        usersListBox = new JTextArea(60,15);
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

    private void showErrorScreen() {
        frame.setVisible(false);
        errorFrame = new JFrame("Wrong data!");



        errorFrame.setVisible(true);
        errorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        errorFrame.setSize(300, 300);
    }

    class MessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (StringUtils.isBlank(messageBox.getText())) {
                // do nothing
            } else {
                String message = messageBox.getText();
                try {
                    ClientService.getInstance().sendMessage(message);
                    messageBox.setText("");
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ConnectData connectData = new ConnectData();
    private String user;

    class ServerButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            user = usernameChooser.getText();
            String ipAddress = ipAddressChooser.getText();
            int port = Integer.parseInt(portChooser.getText());

            connectData.setUser(user);
            connectData.setIpAddress(ipAddress);
            connectData.setPort(port);

            if (user.length() < 1) {
                authorizationFrame.setVisible(false);
                showErrorScreen();
            } else {
                authorizationFrame.setVisible(false);
                showChatScreen();
            }
            try {
                ClientService.getInstance().connectUser(connectData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
