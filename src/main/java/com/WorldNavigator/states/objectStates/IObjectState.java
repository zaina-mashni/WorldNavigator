package com.WorldNavigator.states.objectStates;

import com.WorldNavigator.entities.Object;
import com.WorldNavigator.entities.PlayerInfo;

public interface IObjectState {
    IObjectState handleStateChangeInput(PlayerInfo player, Object object, String command);
    String handleStateSpecificInput(PlayerInfo player, Object object, String input);
    String getName();
}
