package com.WorldNavigator.states.roomStates.roomStatusStates;

import com.WorldNavigator.entities.PlayerInfo;

public interface IRoomStatusState{
    IRoomStatusState handleStateChangeInput(PlayerInfo player, String input);
    String handleStateSpecificInput(PlayerInfo player, String input);
    String getName();
}
