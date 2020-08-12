package com.WorldNavigator.commands;

import com.WorldNavigator.entities.Item;
import com.WorldNavigator.entities.MapInfo;
import com.WorldNavigator.entities.Object;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.features.Container;
import com.WorldNavigator.features.Trade;
import com.WorldNavigator.messages.ErrorMessages;
import com.WorldNavigator.utils.Pair;
import com.WorldNavigator.states.playerStates.TradeLevel;

import java.util.List;

public class Buy implements ICommand {
  @Override
  public String getName() {
    return "buy";
  }

  @Override
  public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
    checkArguments(player, map, splitCommand, "Buy");
    if (!checkNumberOfInput(splitCommand, 2)) {
      return ErrorMessages.invalidInput;
    }
    if (!player.getCurrentState().getName().equals("tradeLevel")) {
      throw new IllegalArgumentException("Reached buy command while not facing seller");
    }
    Object tradeObject = ((TradeLevel) player.getCurrentState()).getObject();
    Trade tradeFeature = ((Trade) tradeObject.getFeature("trade"));
    Container container = tradeFeature.getInventory();
    Pair<Item, Integer> item = getItem(container, splitCommand.get(1));
    if (item == null) {
      return splitCommand.get(1) + " not found in " + tradeObject.getName() + "!";
    }
    if (item.getValue() < 1) {
      throw new IllegalStateException("Item can not remain in inventory if amount is less than 1.");
    }
    if (player.getGoldAmount() >= item.getKey().getCost()) {
      tradeFeature.buy(item.getKey().getName());
      player.updateGoldAmount(item.getKey().getCost() * -1);
      player.getInventory().addItem(item.getKey(), 1);
      return item.getKey().getName() + " bought and acquired!";
    }
    return "Come back when you have enough gold!";
  }
}
