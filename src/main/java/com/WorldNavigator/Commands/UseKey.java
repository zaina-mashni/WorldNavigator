package com.WorldNavigator.Commands;

import com.WorldNavigator.Entities.MapInfo;
import com.WorldNavigator.Entities.Object;
import com.WorldNavigator.Entities.PlayerInfo;

import java.util.List;

public class UseKey implements ICommand {
    @Override
    public String getName() {
        return "useKey";
    }

    @Override
    public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
        if(!checkNumberOfInput(splitCommand,2)){
            return INVALID;
        }
        Object object=getObject(player,splitCommand.get(1),player.getFacingDirection());
        if(object==null){
            return "You are not facing a "+splitCommand.get(1)+"!";
        }
        object.handleStateChangeInput(player, getName());
        return object.handleStateSpecificInput(player,getName());
    }
}
