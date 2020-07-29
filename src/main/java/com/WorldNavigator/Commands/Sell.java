package com.WorldNavigator.Commands;

import com.WorldNavigator.Entities.Item;
import com.WorldNavigator.Entities.MapInfo;
import com.WorldNavigator.Entities.Object;
import com.WorldNavigator.Entities.PlayerInfo;
import com.WorldNavigator.Features.Container;
import com.WorldNavigator.Features.Trade;
import com.WorldNavigator.Pair;
import com.WorldNavigator.States.PlayerStates.TradeLevel;

import java.util.List;

public class Sell implements ICommand {
  @Override
  public String getName() {
    return "sell";
  }

  @Override
  public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
    if (!checkNumberOfInput(splitCommand, 2)) {
      return INVALID;
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
    player.getInventory().replaceItem(item.getKey().getName(),player.getInventory().getAmount(item.getKey().getName())-1);
    return item.getKey().getName() + " sold!";
  }
}
