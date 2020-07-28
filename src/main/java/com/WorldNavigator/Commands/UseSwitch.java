package com.WorldNavigator.Commands;

import com.WorldNavigator.Entities.MapInfo;
import com.WorldNavigator.Entities.PlayerInfo;

import java.util.List;

public class UseSwitch implements ICommand {
    @Override
    public String getName() {
        return "useSwitch";
    }

    @Override
    public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
        if(!checkNumberOfInput(splitCommand,1)){
            return INVALID;
        }
        player.getCurrentRoom().handleStatusStateChangeInput(player,getName());
        return player.getCurrentRoom().handleStatusStateSpecificInput(player,getName());
    }
}
