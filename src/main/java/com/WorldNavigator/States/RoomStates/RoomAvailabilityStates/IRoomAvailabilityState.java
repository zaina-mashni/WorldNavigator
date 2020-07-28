package com.WorldNavigator.States.RoomStates.RoomAvailabilityStates;

import com.WorldNavigator.Entities.PlayerInfo;

public interface IRoomAvailabilityState {
    IRoomAvailabilityState handleStateChangeInput(PlayerInfo player, String input);
    String handleStateSpecificInput(PlayerInfo player, String input);
    String getName();
}
