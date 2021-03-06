package ru.cft.focusstart.dto;

import java.time.LocalDateTime;

public class SendMessageFromServer extends Method {

    private String user;

    private String message;

    private LocalDateTime time;

    public SendMessageFromServer() {
    }

    public SendMessageFromServer(String user, String message) {
        this.user = user;
        this.message = message;
        this.time = LocalDateTime.now();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
