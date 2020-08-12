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

public class Sell implements ICommand {
  @Override
  public String getName() {
    return "sell";
  }

  @Override
  public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
    checkArguments(player, map, splitCommand, "Sell");
    if (!checkNumberOfInput(splitCommand, 2)) {
      return ErrorMessages.invalidInput;
    }
    if (!player.getCurrentState().getName().equals("tradeLevel")) {
      throw new IllegalArgumentException("Reached sell command while not facing seller");
    }
    Object tradeObject = ((TradeLevel) player.getCurrentState()).getObject();
    Trade tradeFeature = ((Trade) tradeObject.getFeature("trade"));
    Container container = player.getInventory();
    Pair<Item, Integer> item = getItem(container, splitCommand.get(1));
    if (item == null) {
      return splitCommand.get(1) + " not found in " + tradeObject.getName() + "!";
    }
    if (item.getValue() < 1) {
      throw new IllegalStateException("Item can not remain in inventory if amount is less than 1.");
    }
    tradeFeature.sell(item.getKey());
    player.updateGoldAmount(item.getKey().getCost());
    player
        .getInventory()
        .replaceItem(
            item.getKey().getName(),
            player.getInventory().getItemAmount(item.getKey().getName()) - 1);
    return item.getKey().getName() + " sold!";
  }
}
