package com.WorldNavigator.commands;

import com.WorldNavigator.entities.MapInfo;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.messages.ErrorMessages;

import java.util.List;

public class Finish implements ICommand {

    @Override
    public String getName() {
        return "finish";
    }

    @Override
    public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
        checkArguments(player,map,splitCommand);
        if(!checkNumberOfInput(splitCommand,1)){
            return ErrorMessages.invalidInput;
        }
        if(player.getCurrentState().getName().equals("roomLevel")){
            throw new IllegalStateException("player should not be able to execute finish command in roomLevel.");
        }
        player.popState();
        return "You are now looking at the "+player.getCurrentState().getName()+"!";
    }
}
