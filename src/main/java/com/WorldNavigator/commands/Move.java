package com.WorldNavigator.commands;

import com.WorldNavigator.entities.MapInfo;
import com.WorldNavigator.entities.Object;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.entities.Room;
import com.WorldNavigator.features.Passage;
import com.WorldNavigator.messages.ErrorMessages;

import java.util.List;

public class Move implements ICommand {
  private int direction;

  public Move(int direction) {
    this.direction = direction;
  }

  @Override
  public String getName() {
    return "move";
  }

  @Override
  public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
    checkArguments(player, map, splitCommand, "Move");
    if (!checkNumberOfInput(splitCommand, 1)) {
      return ErrorMessages.invalidInput;
    }
    Object passageObject = getObjectWithFeature(player, "passage", direction);
    if (passageObject == null) {
      return "You need a door to move to a new room!";
    }
    if (passageObject.getState().getName().equals("opened")) {
      Room oppositeRoom =
          ((Passage) passageObject.getFeature("passage")).getOppositeRoom(player.getCurrentRoom());
      if (!oppositeRoom.getAvailabilityState().getName().equals("full")) {
        player.getCurrentRoom().handleAvailabilityStateChangeInput(player,"leave");
        System.out.println("IN MOVE first "+player.getCurrentRoom().getAvailabilityState().getName());
        oppositeRoom.handleAvailabilityStateChangeInput(player, getName());
        player.setCurrentRoom(oppositeRoom);
        System.out.println("IN MOVE second "+player.getCurrentRoom().getAvailabilityState().getName());
        return player.getCurrentRoom().handlePostAvailabilityStateChangeInput(player, getName());
      }
      return "Two players are currently fighting in this room! return when it's over.";
    }
    return passageObject.handlePostStateChangeInput(player, getName());
  }
}
