package com.WorldNavigator.states.roomStates.roomAvailabilityStates;

import com.WorldNavigator.entities.PlayerInfo;

public class Open implements IRoomAvailabilityState {
    @Override
    public IRoomAvailabilityState handleStateChangeInput(PlayerInfo player, String input) {
        checkIfNull("Player",player,"Open");
        checkIfNull("Input",input,"Open");
        if(input.equals("forward") || input.equals("backward") || input.equals("move")){
            return new Full();
        }
        else if(input.equals("leave")){
            return new Empty();
        }
        return this;
    }

    @Override
    public String handleStateSpecificInput(PlayerInfo player, String input) {
        checkIfNull("Player",player,"Open");
        checkIfNull("Input",input,"Open");
        if(input.equals("forward") || input.equals("backward") || input.equals("move")){
            return "You entered a new room!";
        }
        return null;
    }

    @Override
    public String getName() {
        return "open";
    }

}
