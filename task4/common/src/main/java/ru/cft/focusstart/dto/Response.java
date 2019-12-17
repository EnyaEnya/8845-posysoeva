package ru.cft.focusstart.dto;

import java.util.Collection;

public class Response extends Method {

    private Status status;

    private String errorText;

    private Collection<String> usersList;

    public Response() {
    }

    public Response(Collection<String> usersList) {
        this.usersList = usersList;
    }

    public Response(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }


    public Collection<String> getUsersList() {
        return usersList;
    }

    public void setUsersList(Collection<String> usersList) {
        this.usersList = usersList;
    }

}
