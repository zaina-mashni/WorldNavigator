package com.WorldNavigator.Commands;

import com.WorldNavigator.Entities.MapInfo;
import com.WorldNavigator.Entities.Object;
import com.WorldNavigator.Entities.PlayerInfo;
import com.WorldNavigator.Services.CommandService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Open implements ICommand {

    @Override
    public String getName() {
        return "open";
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
        object.handleStateChangeInput(player,"open");
        return object.handleStateSpecificInput(player,"open");
    }
}
