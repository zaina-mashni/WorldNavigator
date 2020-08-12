package com.WorldNavigator.commands;

import com.WorldNavigator.Direction;
import com.WorldNavigator.entities.MapInfo;
import com.WorldNavigator.entities.Object;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.entities.Room;
import com.WorldNavigator.features.Passage;
import com.WorldNavigator.messages.ErrorMessages;

import java.util.List;

public class Backward implements ICommand {
  @Override
  public String getName() {
    return "backward";
  }

  @Override
  public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
    checkArguments(player, map, splitCommand, "Backward");
    if (!checkNumberOfInput(splitCommand, 1)) {
      return ErrorMessages.invalidInput;
    }
    int oppositeFacingDirection =
        Direction.values()[player.getFacingDirection()].getOppositeDirection(
            player.getFacingDirection());
    return new Move(oppositeFacingDirection).execute(player, map, splitCommand);
  }
}
