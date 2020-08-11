package com.WorldNavigator.states.objectStates;

import com.WorldNavigator.entities.Object;
import com.WorldNavigator.entities.PlayerInfo;

public class Closed implements  IObjectState {
    static final String NAME="closed";
    @Override
    public IObjectState handleStateChangeInput(PlayerInfo player, Object object, String command) {
        if (command.equals("open")) {
            return new Opened();
        }
        return this;
    }

    @Override
    public String handleStateSpecificInput(PlayerInfo player, Object object, String input){
        if(input.equals("check") || input.equals("backward") || input.equals("forward") || input.equals("useKey")){
            return object.getName()+" is closed!";
        }
        return "";
    }

    @Override
    public String getName() {
        return NAME;
    }
}
