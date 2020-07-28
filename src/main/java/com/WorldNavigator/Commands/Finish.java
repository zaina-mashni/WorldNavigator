package com.WorldNavigator.Commands;

import com.WorldNavigator.Entities.MapInfo;
import com.WorldNavigator.Entities.PlayerInfo;

import java.util.List;

public class Finish implements ICommand {

    @Override
    public String getName() {
        return "finish";
    }

    @Override
    public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
        if(!checkNumberOfInput(splitCommand,1)){
            return INVALID;
        }
        if(player.getCurrentState().getName()=="roomLevel"){
            throw new IllegalStateException("player should not be able to execute finish command in roomLevel.");
        }
        player.popState();
        return "You are now looking at the "+player.getCurrentState().getName()+"!";
    }
}
