package ru.cft.focusstart.dto;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "method")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Heartbeat.class, name = "heartbeat"),
        @JsonSubTypes.Type(value = AddUser.class, name = "addUser"),
        @JsonSubTypes.Type(value = ConnectUser.class, name = "connectUser"),
        @JsonSubTypes.Type(value = SendMessageFromUser.class, name = "sendMessageFromUser"),
        @JsonSubTypes.Type(value = GetUsersList.class, name = "getUsersList"),
        @JsonSubTypes.Type(value = RemoveUser.class, name = "removeUser"),
        @JsonSubTypes.Type(value = SendMessageFromServer.class, name = "sendMessageFromServer"),
        @JsonSubTypes.Type(value = Response.class, name = "response")
})
public abstract class Method implements Serializable {

}
