package com.WorldNavigator.states.objectStates;

import com.WorldNavigator.entities.Object;
import com.WorldNavigator.entities.PlayerInfo;

public class Unlocked implements  IObjectState {
    static final String NAME="unlocked";
    @Override
    public IObjectState handleStateChangeInput(PlayerInfo player, Object object, String command) {
        if (command.equals("open")) {
            return new Opened();
        }
        else if(command.equals("useKey")){
            return new Locked();
        }
        return this;
    }

    @Override
    public String handleStateSpecificInput(PlayerInfo player, Object object, String input) {
        if(input.equals("check") || input.equals("backward") || input.equals("forward") || input.equals("useKey")){
            return object.getName()+" is unlocked!";
        }
        return "";
    }

    @Override
    public String getName() {
        return NAME;
    }
}
