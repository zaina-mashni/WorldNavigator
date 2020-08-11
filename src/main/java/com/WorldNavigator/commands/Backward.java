package com.WorldNavigator.commands;

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
    checkArguments(player,map,splitCommand);
    if(!checkNumberOfInput(splitCommand,1)){
      return ErrorMessages.invalidInput;
    }
    int oppositeFacingDirection = (player.getFacingDirection() + 2) % 4;
    Object passageObject = getObjectWithFeature(player,"passage",oppositeFacingDirection);
    if (passageObject == null) {
      return "Your back is not facing a door!";
    }
    if (passageObject.getState().getName().equals("opened")) {
      Room oppositeRoom =
          ((Passage) passageObject.getFeature("passage")).getOppositeRoom(player.getCurrentRoom());
      if(!oppositeRoom.getAvailabilityState().getName().equals("full")){
        player.getCurrentRoom().popAvailabilityState();
       // oppositeRoom.pushAvailabilityState(player);
        oppositeRoom.handleAvailabilityStateChangeInput(player,getName());
        player.setCurrentRoom(oppositeRoom);
        return player.getCurrentRoom().handleAvailabilityStateSpecificInput(player,getName());
      }
      return "Two players are currently fighting in this room! return when it's over.";
    }
    return passageObject.handleStateSpecificInput(player,"backward");
  }

}
