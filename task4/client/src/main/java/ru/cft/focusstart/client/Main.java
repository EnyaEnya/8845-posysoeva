package ru.cft.focusstart.client;

import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    private JFrame frame = new JFrame("Simple chat");
    private JTextField messageBox;
    private JTextArea chatBox;
    private JTextArea userListBox;
    private JTextField usernameChooser;
    private JTextField ipAddressChooser;
    private JTextField portChooser;
    private JFrame authorizationFrame;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Main mainGUI = new Main();
        mainGUI.showLoginScreen();
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

        authorizationPanel.add(choosePortLabel, "gap, sg 1");
        authorizationPanel.add(portChooser, "wrap");


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

        userListBox = new JTextArea(60,15);
        userListBox.setEditable(false);
        userListBox.setLineWrap(true);
        userListBox.append(username);
        eastPanel.add(userListBox);

        JButton sendMessage = new JButton("Send Message");
        sendMessage.setMnemonic(13);
        southPanel.add(sendMessage);

        sendMessage.addActionListener(new MessageButtonListener());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);
    }

    class MessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (StringUtils.isBlank(messageBox.getText())) {
                // do nothing
            } else if (messageBox.getText().equals(".clear")) {
                chatBox.setText("Cleared all messages\n");
                messageBox.setText("");
            } else {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                chatBox.append(dtf.format(now) + " <" + username + ">:  " + messageBox.getText() + System.lineSeparator());
                messageBox.setText("");
            }
        }
    }

    private String username;
    private String ipAddress;
    private String port;

    class ServerButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            username = usernameChooser.getText();
            ipAddress = ipAddressChooser.getText();
            port = portChooser.getText();
            if (username.length() < 1) {
                System.out.println("Enter username!");
            } else {
                authorizationFrame.setVisible(false);
                showChatScreen();
            }
        }

    }
}
