package com.WorldNavigator.states.roomStates.roomStatusStates;

import com.WorldNavigator.entities.PlayerInfo;

public class Dark implements IRoomStatusState {

  @Override
  public IRoomStatusState handleStateChangeInput(PlayerInfo player, String input) {
    checkIfNull("Player",player,"Dark");
    checkIfNull("Input",input,"Dark");
    if (input.equals("useSwitch")) {
      return new Lit();
    }
    return this;
  }

  @Override
  public String handlePostStateChangeInput(PlayerInfo player, String input) {
    checkIfNull("Player",player,"Dark");
    checkIfNull("Input",input,"Dark");
    if (input.equals("look")
        && player.getInventory().containsItem("flashlight")
        && player.getInventory().getItem("flashlight").getState().getName().equals("switchedOn")) {
      return player.getCurrentRoom().getWall(player.getFacingDirection()).toString();
    } else if (input.equals("look")) {
      return "Room is dark! look for a light source.";
    } else if (input.equals("useSwitch")) {
      return "Switch is off!";
    }
    return "";
  }

  @Override
  public String getName() {
    return "dark";
  }

}
