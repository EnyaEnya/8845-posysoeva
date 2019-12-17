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

    private static final long MAX_IDLE_TIME = TimeUnit.SECONDS.toNanos(2);

    private BufferedReader reader;

    private boolean interrupted = false;

    private long lastEventTime;

    private PrintWriter writer;

    public ClientConnection(Socket socket) throws IOException {
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream());
        lastEventTime = System.nanoTime();
    }

    @Override
    public void run() {
        while (!interrupted) {
            if (lastEventTime < System.nanoTime() - MAX_IDLE_TIME) {
                log.info("Heartbeat");
                try {
                    ClientService.INSTANCE.makeHeartbeat();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                lastEventTime = System.nanoTime();
            }
            try {
                String message = null;
                if (reader.ready()) {
                    message = reader.readLine();
                }
                if (message != null) {
                    log.info(message);
                    ClientService.INSTANCE.doService(message);
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

    public void write(String jsonString) {
        writer.println(jsonString);
        writer.flush();
    }

}
