package com.WorldNavigator.States.RoomStates.RoomStatusStates;

import com.WorldNavigator.Entities.PlayerInfo;

public class NoSwitch implements IRoomStatusState {
    @Override
    public IRoomStatusState handleStateChangeInput(PlayerInfo player, String input) {
        //no current command affects this state
        return this;
    }

    @Override
    public String handleStateSpecificInput(PlayerInfo player, String input) {
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
