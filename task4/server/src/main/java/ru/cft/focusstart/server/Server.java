package ru.cft.focusstart.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

public class Server {

    private static final Logger log = LoggerFactory.getLogger(Server.class);

    private static AtomicLong threadCount = new AtomicLong();

    public static void main(String[] args) throws IOException {

        Properties properties = new Properties();
        try (InputStream propertiesStream = Server.class.getResourceAsStream("/server.properties")) {
            if (propertiesStream != null) {
                properties.load(propertiesStream);
            }
        }
        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(properties.getProperty("server.port")));


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                serverSocket.close();
                ConnectionService.INSTANCE.checkoutConnections();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }));


        Thread checkoutConnectionsThread = new Thread(() -> {
            while (true) {
                try {
                    ConnectionService.INSTANCE.checkoutConnections();
                    Thread.sleep(5000);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
        checkoutConnectionsThread.setDaemon(true);
        checkoutConnectionsThread.start();


        //noinspection InfiniteLoopStatement
        while (true) {
            Socket client = null;
            log.info("Waiting for client");
            while (client == null) {
                client = serverSocket.accept();
            }
            log.info("New client from {}", client.getRemoteSocketAddress().toString());
            Thread thread = new Thread(new Connection(client));
            thread.setName("Connection-" + threadCount.getAndIncrement());
            thread.start();
        }
    }
}
