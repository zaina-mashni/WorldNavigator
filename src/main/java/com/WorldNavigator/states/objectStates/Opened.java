package com.WorldNavigator.states.objectStates;

import com.WorldNavigator.entities.Object;
import com.WorldNavigator.entities.PlayerInfo;

public class Opened implements  IObjectState {
    static final String NAME="opened";
    @Override
    public IObjectState handleStateChangeInput(PlayerInfo player, Object object, String command) {
        if (command.equals("close")) {
            return new Closed();
        }
        return this;
    }

    @Override
    public String handleStateSpecificInput(PlayerInfo player, Object object, String input) {
        if(input.equals("check") || input.equals("open")){
            return object.getName()+" is open!";
        }
        return "";
    }

    @Override
    public String getName() {
        return NAME;
    }
}
