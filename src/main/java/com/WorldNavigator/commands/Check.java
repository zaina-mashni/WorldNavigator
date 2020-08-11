package com.WorldNavigator.commands;

import com.WorldNavigator.entities.MapInfo;
import com.WorldNavigator.entities.Object;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.features.Trade;
import com.WorldNavigator.messages.ErrorMessages;
import com.WorldNavigator.states.playerStates.ObjectLevel;
import com.WorldNavigator.states.playerStates.TradeLevel;

import java.util.List;

public class Check implements ICommand {
    @Override
    public String getName() {
        return "check";
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

        if(object.getState().getName().equals("opened") || object.getState().getName().equals("static")){
            if(object.hasFeature("trade")){
                player.pushState(new TradeLevel(object));
                return ((Trade) object.getFeature("trade")).getInventory().toString();
            }
            if(object.hasFeature("container")){
                player.pushState(new ObjectLevel(object));
                return object.getFeature("container").toString();
            }
        }
        return object.handleStateSpecificInput(player,"check");
    }
}
