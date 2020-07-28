package com.WorldNavigator.Requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommandRequest {
    private String command;
    private String username;

    public CommandRequest(
            @JsonProperty(value = "username", required = true) String username,
            @JsonProperty(value = "command", required = true) String command
            ) {
        this.username = username;
        this.command = command;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getUsername() {
        return username;
    }

    public String getCommand() {
        return command;
    }
}
