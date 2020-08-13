package com.WorldNavigator.states.objectStates;

import com.WorldNavigator.entities.Object;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.features.Lock;

public class Locked implements IObjectState {
  static final String NAME = "locked";

  @Override
  public IObjectState handleStateChangeInput(PlayerInfo player, Object object, String input) {
    checkIfNull("Player",player,"Locked");
    checkIfNull("Object",object,"Locked");
    checkIfNull("Input",input,"Locked");
    if (input.equals("useKey")) {
      if (player.hasItemInInventory(((Lock) object.getFeature("lock")).getKeyName())) {
        return new Unlocked();
      }
    }
    return this;
  }

  @Override
  public String handlePostStateChangeInput(PlayerInfo player, Object object, String input) {
    checkIfNull("Player",player,"Locked");
    checkIfNull("Object",object,"Locked");
    checkIfNull("Input",input,"Locked");
    if(input.equals("check") || input.equals("backward") || input.equals("forward") || input.equals("open") || input.equals("useKey")){
      return object.getName()+" is locked. "+((Lock)object.getFeature("lock")).getKeyName()+" is needed to unlock!";
    }

    return "";
  }

  @Override
  public String getName() {
    return NAME;
  }

}
