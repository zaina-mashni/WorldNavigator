package com.WorldNavigator.States.RoomStates.RoomStatusStates;

import com.WorldNavigator.Entities.PlayerInfo;

public class Lit implements IRoomStatusState {
    @Override
    public IRoomStatusState handleStateChangeInput(PlayerInfo player, String input) {
        if(input.equals("useSwitch")){
            return new Dark();
        }
        return this;
    }

    @Override
    public String handleStateSpecificInput(PlayerInfo player, String input) {
        if (input.equals("look")){
            return player.getCurrentRoom().getWall(player.getFacingDirection()).toString();
        }
        else if(input.equals("useSwitch")){
            return "Switch is on!";
        }
        return "";
    }

    @Override
    public String getName() {
        return "lit";
    }
}
