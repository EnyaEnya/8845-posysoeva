package ru.cft.focusstart.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(Connection.class);

    private Socket socket;

    private String user;

    private BufferedReader reader;

    private PrintWriter writer;

    private long lastEventTime;

    private boolean interrupted = false;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream());
        lastEventTime = System.nanoTime();
    }

    @Override
    public void run() {
        while (!interrupted) {
            try {
                String message = null;
                if (reader.ready()) {
                    message = reader.readLine();
                    lastEventTime = System.nanoTime();
                }
                if (message != null) {
                    ConnectionService.INSTANCE.doService(message, this);
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

    public void writeResponse(String response) {
        writer.println(response);
        writer.flush();
    }

    public void close() throws IOException {
        try {
            socket.close();
        } finally {
            interrupted = true;
        }
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getLastEventTime() {
        return lastEventTime;
    }
}
