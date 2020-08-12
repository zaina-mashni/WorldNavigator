package com.WorldNavigator.commands;

import com.WorldNavigator.entities.MapInfo;
import com.WorldNavigator.entities.Object;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.messages.ErrorMessages;
import com.WorldNavigator.states.playerStates.TradeLevel;

import java.util.List;

public class Trade implements ICommand {
  @Override
  public String getName() {
    return "trade";
  }

  @Override
  public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
    checkArguments(player, map, splitCommand, "Trade");
    if (!checkNumberOfInput(splitCommand, 1)) {
      return ErrorMessages.invalidInput;
    }

    Object tradeObject = getObjectWithFeature(player, "trade", player.getFacingDirection());
    if (tradeObject == null) {
      return "You are not facing a seller!";
    }

    player.pushState(new TradeLevel(tradeObject));
    return tradeObject.handleStateSpecificInput(player, "check");
  }
}
