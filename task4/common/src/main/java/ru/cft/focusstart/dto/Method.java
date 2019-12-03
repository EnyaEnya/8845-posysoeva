package ru.cft.focusstart.dto;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "method")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConnectUser.class, name = "connectUser"),
        @JsonSubTypes.Type(value = SendMessageFromUser.class, name = "sendMessageFromUser"),
        @JsonSubTypes.Type(value = GetUsersList.class, name = "getUsersList"),
        @JsonSubTypes.Type(value = RemoveUser.class, name = "removeUser"),
        @JsonSubTypes.Type(value = SendMessageFromServer.class, name = "sendMessageFromServer")
})
public abstract class Method implements Serializable {

    private String method;
}
