package com.WorldNavigator.states.objectStates;

import com.WorldNavigator.entities.Object;
import com.WorldNavigator.entities.PlayerInfo;

public class Opened implements  IObjectState {
    static final String NAME="opened";
    @Override
    public IObjectState handleStateChangeInput(PlayerInfo player, Object object, String input) {
        checkIfNull("Player",player,"Opened");
        checkIfNull("Object",object,"Opened");
        checkIfNull("Input",input,"Opened");
        if (input.equals("close")) {
            return new Closed();
        }
        return this;
    }

    @Override
    public String handlePostStateChangeInput(PlayerInfo player, Object object, String input) {
        checkIfNull("Player",player,"Opened");
        checkIfNull("Object",object,"Opened");
        checkIfNull("Input",input,"Opened");
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
