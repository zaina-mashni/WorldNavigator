package com.WorldNavigator.states.roomStates.roomAvailabilityStates;

import com.WorldNavigator.entities.PlayerInfo;

public interface IRoomAvailabilityState {
    IRoomAvailabilityState handleStateChangeInput(PlayerInfo player, String input);
    String handleStateSpecificInput(PlayerInfo player, String input);
    String getName();
}
