package com.WorldNavigator.Requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateRequest {
    private String username;
    private String worldName;
    private String mapFile;

    public CreateRequest(
            @JsonProperty(value = "username", required = true) String username,
            @JsonProperty(value = "worldName", required = true) String worldName,
            @JsonProperty(value = "mapFile", required = true) String mapFile) {
        this.username = username;
        this.worldName = worldName;
        this.mapFile=mapFile;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public void setMapFile(String mapFile) {
        this.mapFile = mapFile;
    }

    public String getUsername() {
        return username;
    }

    public String getWorldName() {
        return worldName;
    }

    public String getMapFile() {
        return mapFile;
    }
}
