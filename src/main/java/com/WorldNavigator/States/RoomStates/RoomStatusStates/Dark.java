package com.WorldNavigator.States.RoomStates.RoomStatusStates;

import com.WorldNavigator.Entities.PlayerInfo;

public class Dark implements IRoomStatusState {

    @Override
    public IRoomStatusState handleStateChangeInput(PlayerInfo player, String input) {
        if(input.equals("useSwitch")){
            return new Lit();
        }
        return this;
    }

    @Override
    public String handleStateSpecificInput(PlayerInfo player, String input) {
        if (input.equals("look")){
            return "Room is dark! look for a light source.";
        }
        else if(input.equals("useSwitch")){
            return "Switch is off!";
        }
        return "";
    }

    @Override
    public String getName() {
        return "dark";
    }
}
