package com.WorldNavigator.reply;

import com.WorldNavigator.views.GameInfoView;

import java.util.ArrayList;
import java.util.List;

public class ListGamesReply {
    public List<GameInfoView> availableGames;
    public String status;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public ListGamesReply(){
        availableGames=new ArrayList<>();
    }

    public void setAvailableGames(List<GameInfoView> availableGames) {
        this.availableGames = availableGames;
    }

    public List<GameInfoView> getAvailableGames() {
        return availableGames;
    }
}
