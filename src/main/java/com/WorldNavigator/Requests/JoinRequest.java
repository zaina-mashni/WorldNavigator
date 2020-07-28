package com.WorldNavigator.Requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JoinRequest {
    private String username;
    private String worldName;

    public JoinRequest(
            @JsonProperty(value = "username", required = true) String username,
            @JsonProperty(value = "worldName", required = true) String worldName) {
        this.username = username;
        this.worldName = worldName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public String getUsername() {
        return username;
    }

    public String getWorldName() {
        return worldName;
    }
}
