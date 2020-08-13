package com.WorldNavigator.commands;

import com.WorldNavigator.entities.MapInfo;
import com.WorldNavigator.entities.Object;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.messages.ErrorMessages;

import java.util.List;

public class Open implements ICommand {

  @Override
  public String getName() {
    return "open";
  }

  @Override
  public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
    checkArguments(player, map, splitCommand, "Open");
    if (!checkNumberOfInput(splitCommand, 2)) {
      return ErrorMessages.invalidInput;
    }
    Object object = getObject(player, splitCommand.get(1), player.getFacingDirection());
    if (object == null) {
      return "You are not facing a " + splitCommand.get(1) + "!";
    }
    object.handleStateChangeInput(player, "open");
    return object.handlePostStateChangeInput(player, "open");
  }
}
