package com.WorldNavigator.views;

public class GameInfoView {
    String worldName;
    int playerCapacity;
    int playersJoined;
    String adminName;
    boolean started;

    public GameInfoView(String worldName, int playerCapacity,int playersJoined,String adminName, boolean started) {
        this.worldName = worldName;
        this.playerCapacity=playerCapacity;
        this.playersJoined=playersJoined;
        this.adminName=adminName;
        this.started=started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isStarted() {
        return started;
    }

    public void setPlayerCapacity(int playerCapacity) {
        this.playerCapacity = playerCapacity;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public void setPlayersJoined(int playersJoined) {
        this.playersJoined = playersJoined;
    }

    public String getWorldName() {
        return worldName;
    }

    public int getPlayersJoined() {
        return playersJoined;
    }

    public int getPlayerCapacity() {
        return playerCapacity;
    }

    public String getAdminName() {
        return adminName;
    }
}
