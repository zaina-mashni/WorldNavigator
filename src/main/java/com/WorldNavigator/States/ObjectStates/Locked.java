package com.WorldNavigator.States.ObjectStates;

import com.WorldNavigator.Entities.Object;
import com.WorldNavigator.Entities.PlayerInfo;
import com.WorldNavigator.Features.Lock;

public class Locked implements IObjectState {
  static final String NAME = "locked";

  @Override
  public IObjectState handleStateChangeInput(PlayerInfo player, Object object, String command) {
    if (command.equals("useKey")) {
      if (player.hasItemInInventory(((Lock) object.getFeature("lock")).getKeyName())) {
        return new Unlocked();
      }
    }
    return this;
  }

  @Override
  public String handleStateSpecificInput(PlayerInfo player, Object object, String input) {
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
