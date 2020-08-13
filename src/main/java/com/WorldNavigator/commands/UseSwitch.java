package com.WorldNavigator.commands;

import com.WorldNavigator.entities.MapInfo;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.messages.ErrorMessages;

import java.util.List;

public class UseSwitch implements ICommand {
  @Override
  public String getName() {
    return "useSwitch";
  }

  @Override
  public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
    checkArguments(player, map, splitCommand, "UseSwitch");
    if (!checkNumberOfInput(splitCommand, 1)) {
      return ErrorMessages.invalidInput;
    }
    player.getCurrentRoom().handleStatusStateChangeInput(player, getName());
    return player.getCurrentRoom().handlePostStatusStateChangeInput(player, getName());
  }
}
