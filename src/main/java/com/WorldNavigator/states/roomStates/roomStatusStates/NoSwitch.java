package com.WorldNavigator.states.roomStates.roomStatusStates;

import com.WorldNavigator.entities.PlayerInfo;

public class NoSwitch implements IRoomStatusState {
    @Override
    public IRoomStatusState handleStateChangeInput(PlayerInfo player, String input) {
        checkIfNull("Player",player,"NoSwitch");
        checkIfNull("Input",input,"NoSwitch");
        //no current command affects this state
        return this;
    }

    @Override
    public String handlePostStateChangeInput(PlayerInfo player, String input) {
        checkIfNull("Player",player,"NoSwitch");
        checkIfNull("Input",input,"NoSwitch");
        if (input.equals("look")){
            return "Room is dark! look for a light source.";
        }
        else if(input.equals("useSwitch")){
            return "No switch! Look for another light source.";
        }
        return "";
    }

    @Override
    public String getName() {
        return "noSwitch";
    }

}
