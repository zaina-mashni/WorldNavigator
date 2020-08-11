package com.WorldNavigator.commands;

import com.WorldNavigator.entities.MapInfo;
import com.WorldNavigator.entities.Object;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.features.Trade;
import com.WorldNavigator.messages.ErrorMessages;
import com.WorldNavigator.states.playerStates.TradeLevel;

import java.util.List;

public class ListSellerItems implements ICommand {
    @Override
    public String getName() {
        return "listSellerItems";
    }

    @Override
    public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
        checkArguments(player,map,splitCommand);
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
