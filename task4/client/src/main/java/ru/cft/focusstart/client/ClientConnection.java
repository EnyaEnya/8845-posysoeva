package ru.cft.focusstart.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ClientConnection implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ClientConnection.class);

    private static final long MAX_IDLE_TIME = TimeUnit.SECONDS.toNanos(5);

    private Socket socket;

    private BufferedReader reader;

    private PrintWriter writer;

    private boolean interrupted = false;

    private long lastMessage;

    public ClientConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.lastMessage = System.nanoTime();
    }

    @Override
    public void run() {
        while (!interrupted) {
            if (lastMessage < System.nanoTime() - MAX_IDLE_TIME) {
                log.info("Heartbeat");
                try {
                    ClientService.getInstance().makeHeartbeat();
                    //ClientService.getInstance().getUsersList();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                lastMessage = System.nanoTime();
            }
            try {
                String message = null;
                if (reader.ready()) {
                    message = reader.readLine();
                }
                if (message != null) {
                    log.info(message);
                    ClientService.getInstance().doService(message);
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                interrupted = true;
            }
        }
        log.info("Connection {} closed", Thread.currentThread().getName());
    }

}
