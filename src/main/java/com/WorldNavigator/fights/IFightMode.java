package com.WorldNavigator.fights;

import com.WorldNavigator.commands.ICommand;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.GameControl;

import java.util.List;

public interface IFightMode {
    List<ICommand> getAvailableCommands();
    void setPlayerChoice(PlayerInfo player, ICommand choice, GameControl gameControl);
    boolean isReady();
    PlayerInfo getWinningPlayer();
    PlayerInfo getLosingPlayer();
    String getResultOfFightForPlayer(PlayerInfo player);
    int getRoomId();
    String getWorldName();
}
