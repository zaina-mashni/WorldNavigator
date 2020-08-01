package com.WorldNavigator.Commands;

import com.WorldNavigator.Entities.MapInfo;
import com.WorldNavigator.Entities.Object;
import com.WorldNavigator.Entities.PlayerInfo;
import com.WorldNavigator.Features.Trade;
import com.WorldNavigator.Messages.ErrorMessages;
import com.WorldNavigator.States.PlayerStates.TradeLevel;

import java.util.List;

public class ListSellerItems implements ICommand {
    @Override
    public String getName() {
        return "listSellerItems";
    }

    @Override
    public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
        if (!checkNumberOfInput(splitCommand,1)) {
            return ErrorMessages.invalidInput;
        }
        if (!player.getCurrentState().getName().equals("tradeLevel")) {
            throw new IllegalArgumentException("Reached listSellerItems command while not facing seller");
        }
        Object tradeObject = ((TradeLevel) player.getCurrentState()).getObject();
        Trade tradeFeature = ((Trade) tradeObject.getFeature("trade"));
        return tradeFeature.getInventory().toString();
    }
}
