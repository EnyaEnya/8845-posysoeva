package ru.cft.focusstart.dto;

import java.util.Collection;

public class UsersList extends Response {

    private Collection<String> usersList;

    public UsersList() {
    }

    public UsersList(Collection<String> usersList) {
        this.usersList = usersList;
    }

    public Collection<String> getUsersList() {
        return usersList;
    }

    public void setUsersList(Collection<String> usersList) {
        this.usersList = usersList;
    }
}
