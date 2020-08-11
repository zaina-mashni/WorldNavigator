package com.WorldNavigator.commands;

import com.WorldNavigator.Direction;
import com.WorldNavigator.entities.MapInfo;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.messages.ErrorMessages;

import java.util.List;

public class Left implements ICommand {
    @Override
    public String getName() {
        return "left";
    }

    @Override
    public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
        checkArguments(player,map,splitCommand);
        if(!checkNumberOfInput(splitCommand,1)){
            return ErrorMessages.invalidInput;
        }
        int tmpFacingDirection = (player.getFacingDirection() - 1) % 4;
        while (tmpFacingDirection < 0) {
            tmpFacingDirection += 4;
        }
        player.setFacingDirection(tmpFacingDirection);
        return "You are now facing "+ Direction.values()[player.getFacingDirection()]+"!";
    }
}
