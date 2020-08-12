package com.WorldNavigator.fights;

import com.WorldNavigator.commands.ICommand;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.entities.GameControl;

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
    default void checkIfNull(String key, java.lang.Object value, String className) {
        if (value == null) {
            throw new IllegalArgumentException(key + " can not be null in class "+className);
        }
    }
}
