package ru.cft.focusstart.dto;

public class AddUser extends Method {

    private String user;

    public AddUser() {
    }

    public AddUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
