package com.WorldNavigator.reply;

public class GameReply {
    public String message;
    public String facingDirection;
    public String inventory;
    public int timeLeftInMinutes;
    public String availableCommands;
    public boolean isFighting;
    public boolean isPlaying;
    public String status;
    public String worldName;
    public boolean isAdmin;

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFacingDirection(String facingDirection) {
        this.facingDirection = facingDirection;
    }

    public void setAvailableCommands(String availableCommands) {
        this.availableCommands = availableCommands;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public void setTimeLeftInMinutes(int timeLeftInMinutes) {
        this.timeLeftInMinutes = timeLeftInMinutes;
    }

    public int getTimeLeftInMinutes() {
        return timeLeftInMinutes;
    }

    public String getAvailableCommands() {
        return availableCommands;
    }

    public String getFacingDirection() {
        return facingDirection;
    }

    public String getInventory() {
        return inventory;
    }

    public String getMessage(){
        return message;
    }

    public void setFighting(boolean fighting) {
        isFighting = fighting;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

}
