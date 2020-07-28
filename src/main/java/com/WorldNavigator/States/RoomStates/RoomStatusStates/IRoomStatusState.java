package com.WorldNavigator.States.RoomStates.RoomStatusStates;

import com.WorldNavigator.Entities.PlayerInfo;

public interface IRoomStatusState{
    IRoomStatusState handleStateChangeInput(PlayerInfo player, String input);
    String handleStateSpecificInput(PlayerInfo player, String input);
    String getName();
}
