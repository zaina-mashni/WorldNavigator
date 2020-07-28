package com.WorldNavigator.States.ObjectStates;

import com.WorldNavigator.Entities.Object;
import com.WorldNavigator.Entities.PlayerInfo;

public interface IObjectState {
    IObjectState handleStateChangeInput(PlayerInfo player, Object object, String command);

    String handleStateSpecificInput(PlayerInfo player, Object object, String input);
    String getName();
}
