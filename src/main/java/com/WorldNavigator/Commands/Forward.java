package com.WorldNavigator.Commands;

import com.WorldNavigator.Entities.MapInfo;
import com.WorldNavigator.Entities.Object;
import com.WorldNavigator.Entities.PlayerInfo;
import com.WorldNavigator.Entities.Room;
import com.WorldNavigator.Features.Passage;
import com.WorldNavigator.Messages.ErrorMessages;

import java.util.List;

public class Forward implements ICommand {
  @Override
  public String getName() {
    return "forward";
  }

  @Override
  public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
    if(!checkNumberOfInput(splitCommand,1)){
      return ErrorMessages.invalidInput;
    }
    Object passageObject = getObjectWithFeature(player,"passage",player.getFacingDirection());
    if (passageObject == null) {
      return "You are not facing a door!";
    }
    if (passageObject.getState().getName().equals("opened")) {
      Room oppositeRoom =
          ((Passage) passageObject.getFeature("passage")).getOppositeRoom(player.getCurrentRoom());
      if(!oppositeRoom.getAvailabilityState().getName().equals("full")){
        player.getCurrentRoom().popAvailabilityState();
        oppositeRoom.pushAvailabilityState(player);
        player.setCurrentRoom(oppositeRoom);
        return player.getCurrentRoom().handleAvailabilityStateSpecificInput(player,getName());
      }
      return "Two players are currently fighting in this room! return when it's over.";
    }
    return passageObject.handleStateSpecificInput(player,"forward");
  }
}
