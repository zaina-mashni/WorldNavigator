package com.WorldNavigator.Commands;

import com.WorldNavigator.Entities.MapInfo;
import com.WorldNavigator.Entities.Object;
import com.WorldNavigator.Entities.PlayerInfo;
import com.WorldNavigator.Messages.ErrorMessages;
import com.WorldNavigator.States.PlayerStates.TradeLevel;

import java.util.List;

public class Trade implements ICommand {
    @Override
    public String getName() {
        return "trade";
    }

    @Override
    public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
        if(!checkNumberOfInput(splitCommand,1)){
            return ErrorMessages.invalidInput;
        }

        Object tradeObject=getObjectWithFeature(player,"trade",player.getFacingDirection());
        if(tradeObject==null){
            return "You are not facing a seller!";
        }

        player.pushState(new TradeLevel(tradeObject));
        return tradeObject.handleStateSpecificInput(player,"check");
    }
}
