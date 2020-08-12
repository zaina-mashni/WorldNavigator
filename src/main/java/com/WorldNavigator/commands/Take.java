package com.WorldNavigator.commands;

import com.WorldNavigator.entities.MapInfo;
import com.WorldNavigator.entities.Object;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.features.Container;
import com.WorldNavigator.messages.ErrorMessages;
import com.WorldNavigator.states.playerStates.ObjectLevel;

import java.util.List;

public class Take implements ICommand {
  @Override
  public String getName() {
    return "take";
  }

  @Override
  public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
    checkArguments(player, map, splitCommand, "Take");
    if (!checkNumberOfInput(splitCommand, 1)) {
      return ErrorMessages.invalidInput;
    }
    if (!player.getCurrentState().getName().equals("objectLevel")) {
      throw new IllegalStateException(
          "player not in objectLevel state while performing take command.");
    }
    Object object = ((ObjectLevel) player.getCurrentState()).getObject();
    player.addToInventory(((Container) object.getFeature("container")).getItems());
    ((Container) object.getFeature("container")).removeAll();
    return "Items added to your inventory!";
  }
}
