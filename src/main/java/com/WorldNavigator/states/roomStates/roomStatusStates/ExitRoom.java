package com.WorldNavigator.states.roomStates.roomStatusStates;

import com.WorldNavigator.entities.PlayerInfo;

public class ExitRoom implements IRoomStatusState {
    @Override
    public IRoomStatusState handleStateChangeInput(PlayerInfo player, String input) {
        checkIfNull("Player",player,"ExitRoom");
        checkIfNull("Input",input,"ExitRoom");
        //no current command affects this state
        return this;
    }

    @Override
    public String handlePostStateChangeInput(PlayerInfo player, String input) {
        checkIfNull("Player",player,"ExitRoom");
        checkIfNull("Input",input,"ExitRoom");
        if(input.equals("forward") || input.equals("backward")){
            return "You found your way out of the map!";
        }
        return "";
    }

    @Override
    public String getName() {
        return "exit";
    }

}
