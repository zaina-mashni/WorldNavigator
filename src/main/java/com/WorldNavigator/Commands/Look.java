package com.WorldNavigator.Commands;

import com.WorldNavigator.Entities.MapInfo;
import com.WorldNavigator.Entities.PlayerInfo;
import com.WorldNavigator.Messages.ErrorMessages;
import com.WorldNavigator.States.PlayerStates.WallLevel;

import java.util.List;

public class Look implements ICommand {
  @Override
  public String getName() {
    return "look";
  }

  @Override
  public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
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
