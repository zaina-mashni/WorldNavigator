package com.WorldNavigator.commands;

import com.WorldNavigator.entities.MapInfo;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.messages.ErrorMessages;
import com.WorldNavigator.states.playerStates.WallLevel;

import java.util.List;

public class Look implements ICommand {
  @Override
  public String getName() {
    return "look";
  }

  @Override
  public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
    checkArguments(player,map,splitCommand);
    if (!checkNumberOfInput(splitCommand, 1)) {
      return ErrorMessages.invalidInput;
    }
    if (player.getCurrentRoom().getStatusState().getName().equals("lit")
        || (player.hasItemInInventory("flashlight")
            && player
                .getItemFromInventory("flashlight")
                .getState()
                .getName()
                .equals("switchedOn"))) {
      player.pushState(new WallLevel());
    }
    return player.getCurrentRoom().handleStatusStateSpecificInput(player, getName());
}
}
