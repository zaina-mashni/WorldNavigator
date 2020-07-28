package com.WorldNavigator.States.RoomStates.RoomAvailabilityStates;

import com.WorldNavigator.Entities.PlayerInfo;

public class Empty implements IRoomAvailabilityState {
    @Override
    public IRoomAvailabilityState handleStateChangeInput(PlayerInfo player, String input) {
        if(input.equals("forward") || input.equals("backward")){
            return new Open();
        }
        return this;
    }

    @Override
    public String handleStateSpecificInput(PlayerInfo player, String input) {
        return "";
    }

    @Override
    public String getName() {
        return "empty";
    }
}
