package com.WorldNavigator.states.roomStates.roomStatusStates;

import com.WorldNavigator.entities.PlayerInfo;

public class Lit implements IRoomStatusState {
    @Override
    public IRoomStatusState handleStateChangeInput(PlayerInfo player, String input) {
        checkIfNull("Player",player,"Lit");
        checkIfNull("Input",input,"Lit");
        if(input.equals("useSwitch")){
            return new Dark();
        }
        return this;
    }

    @Override
    public String handlePostStateChangeInput(PlayerInfo player, String input) {
        checkIfNull("Player",player,"Lit");
        checkIfNull("Input",input,"Lit");
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
