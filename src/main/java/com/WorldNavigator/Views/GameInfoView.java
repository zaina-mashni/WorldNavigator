package com.WorldNavigator.Views;

public class GameInfoView {
    String worldName;
    int playerCapacity;
    int playersJoined;
    String adminName;

    public GameInfoView(String worldName, int playerCapacity,int playersJoined,String adminName) {
        this.worldName = worldName;
        this.playerCapacity=playerCapacity;
        this.playersJoined=playersJoined;
        this.adminName=adminName;
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
