package com.WorldNavigator.Reply;

import com.WorldNavigator.Views.GameInfoView;

import java.util.List;

public class ListGamesReply {
    public List<GameInfoView> availableGames;

    public void setAvailableGames(List<GameInfoView> availableGames) {
        this.availableGames = availableGames;
    }

    public List<GameInfoView> getAvailableGames() {
        return availableGames;
    }
}
