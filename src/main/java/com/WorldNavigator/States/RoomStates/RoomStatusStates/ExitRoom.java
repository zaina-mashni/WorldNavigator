package com.WorldNavigator.States.RoomStates.RoomStatusStates;

import com.WorldNavigator.Entities.PlayerInfo;

public class ExitRoom implements IRoomStatusState {
    @Override
    public IRoomStatusState handleStateChangeInput(PlayerInfo player, String input) {
        //no current command affects this state
        return this;
    }

    @Override
    public String handleStateSpecificInput(PlayerInfo player, String input) {
        if(input.equals("forward") || input.equals("backward")){
            return "You are the winner";
        }
        return "";
    }

    @Override
    public String getName() {
        return "exit";
    }
}
