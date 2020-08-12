package com.WorldNavigator.states.roomStates.roomAvailabilityStates;

import com.WorldNavigator.entities.PlayerInfo;

public class Empty implements IRoomAvailabilityState {
    @Override
    public IRoomAvailabilityState handleStateChangeInput(PlayerInfo player, String input) {
        checkIfNull("Player",player,"Empty");
        checkIfNull("Input",input,"Empty");
        if(input.equals("forward") || input.equals("backward") || input.equals("move")){
            return new Open();
        }
        return this;
    }

    @Override
    public String handleStateSpecificInput(PlayerInfo player, String input) {
        checkIfNull("Player",player,"Empty");
        checkIfNull("Input",input,"Empty");
        //no current commands are executed on empty room
        return "";
    }

    @Override
    public String getName() {
        return "empty";
    }

}
