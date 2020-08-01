package com.WorldNavigator.Commands;

import com.WorldNavigator.Entities.MapInfo;
import com.WorldNavigator.Entities.Object;
import com.WorldNavigator.Entities.PlayerInfo;
import com.WorldNavigator.Features.Trade;
import com.WorldNavigator.Messages.ErrorMessages;
import com.WorldNavigator.States.PlayerStates.ObjectLevel;
import com.WorldNavigator.States.PlayerStates.TradeLevel;

import java.util.List;

public class Check implements ICommand {
    @Override
    public String getName() {
        return "check";
    }

    @Override
    public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
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
