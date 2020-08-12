package com.WorldNavigator.commands;

import com.WorldNavigator.entities.MapInfo;
import com.WorldNavigator.entities.Object;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.entities.Room;
import com.WorldNavigator.features.Passage;
import com.WorldNavigator.messages.ErrorMessages;

import java.util.List;

public class Forward implements ICommand {
  @Override
  public String getName() {
    return "forward";
  }

  @Override
  public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
    checkArguments(player, map, splitCommand, "Forward");
    if (!checkNumberOfInput(splitCommand, 1)) {
      return ErrorMessages.invalidInput;
    }
    return (new Move(player.getFacingDirection()).execute(player, map, splitCommand));
  }
}
