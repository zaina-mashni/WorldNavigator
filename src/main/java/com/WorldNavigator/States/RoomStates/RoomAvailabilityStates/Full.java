package com.WorldNavigator.States.RoomStates.RoomAvailabilityStates;

import com.WorldNavigator.Entities.PlayerInfo;

public class Full implements IRoomAvailabilityState {
    @Override
    public IRoomAvailabilityState handleStateChangeInput(PlayerInfo player, String input) {
        if(input.equals("leave")){
            return new Open();
        }
        return this;
    }

    @Override
    public String handleStateSpecificInput(PlayerInfo player, String input) {
        if(input.equals("forward") || input.equals("backward")){
            return "Elimination time! May the richest player win.";
        }
        return "";
    }

    @Override
    public String getName() {
        return "full";
    }
}
