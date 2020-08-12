package com.WorldNavigator.commands;

import com.WorldNavigator.Direction;
import com.WorldNavigator.entities.MapInfo;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.messages.ErrorMessages;

import java.util.List;

public class Right implements ICommand {
  @Override
  public String getName() {
    return "right";
  }

  @Override
  public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
    checkArguments(player, map, splitCommand, "Right");
    if (!checkNumberOfInput(splitCommand, 1)) {
      return ErrorMessages.invalidInput;
    }
    int tmpFacingDirection = (player.getFacingDirection() + 1) % 4;
    player.setFacingDirection(tmpFacingDirection);
    return "You are now facing " + Direction.values()[player.getFacingDirection()] + "!";
  }
}
