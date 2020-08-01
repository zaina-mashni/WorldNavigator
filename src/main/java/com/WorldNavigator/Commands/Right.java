package com.WorldNavigator.Commands;

import com.WorldNavigator.Direction;
import com.WorldNavigator.Entities.MapInfo;
import com.WorldNavigator.Entities.PlayerInfo;
import com.WorldNavigator.Messages.ErrorMessages;

import java.util.List;

public class Right implements ICommand {
    @Override
    public String getName() {
        return "right";
    }

    @Override
    public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
        if(!checkNumberOfInput(splitCommand,1)){
            return ErrorMessages.invalidInput;
        }
        int tmpFacingDirection = (player.getFacingDirection() + 1) % 4;
        player.setFacingDirection(tmpFacingDirection);
        return "You are now facing "+ Direction.values()[player.getFacingDirection()]+"!";
    }
}
