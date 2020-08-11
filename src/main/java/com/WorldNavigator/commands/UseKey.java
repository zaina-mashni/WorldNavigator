package com.WorldNavigator.commands;

import com.WorldNavigator.entities.MapInfo;
import com.WorldNavigator.entities.Object;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.messages.ErrorMessages;

import java.util.List;

public class UseKey implements ICommand {
    @Override
    public String getName() {
        return "useKey";
    }

    @Override
    public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
        checkArguments(player,map,splitCommand);
        if(!checkNumberOfInput(splitCommand,2)){
            return ErrorMessages.invalidInput;
        }
        Object object=getObject(player,splitCommand.get(1),player.getFacingDirection());
        if(object==null){
            return "You are not facing a "+splitCommand.get(1)+"!";
        }
        object.handleStateChangeInput(player, getName());
        return object.handleStateSpecificInput(player,getName());
    }
}
