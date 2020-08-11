package com.WorldNavigator.reply;

import com.WorldNavigator.views.GameInfoView;

import java.util.ArrayList;
import java.util.List;

public class ListGamesReply {
    public List<GameInfoView> availableGames;

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
