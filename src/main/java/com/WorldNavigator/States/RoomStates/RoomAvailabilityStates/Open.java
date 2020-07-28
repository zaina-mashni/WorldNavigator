package com.WorldNavigator.States.RoomStates.RoomAvailabilityStates;

import com.WorldNavigator.Entities.PlayerInfo;

public class Open implements IRoomAvailabilityState {
    @Override
    public IRoomAvailabilityState handleStateChangeInput(PlayerInfo player, String input) {
        if(input.equals("forward") || input.equals("backward")){
            return new Full();
        }
        else if(input.equals("leave")){
            return new Empty();
        }
        return this;
    }

    @Override
    public String handleStateSpecificInput(PlayerInfo player, String input) {
        if(input.equals("forward") || input.equals("backward")){
            return "You entered a new room!";
        }
        return null;
    }

    @Override
    public String getName() {
        return "open";
    }
}
