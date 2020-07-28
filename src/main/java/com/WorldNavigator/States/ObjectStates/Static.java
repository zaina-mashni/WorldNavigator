package com.WorldNavigator.States.ObjectStates;

import com.WorldNavigator.Entities.Object;
import com.WorldNavigator.Entities.PlayerInfo;
import com.WorldNavigator.Features.Container;

public class Static implements  IObjectState {
    static final String NAME="static";
    @Override
    public IObjectState handleStateChangeInput(PlayerInfo player, Object object, String command) {
        //no current commands that affect static objects
        return this;
    }

    @Override
    public String handleStateSpecificInput(PlayerInfo player, Object object, String input) {
        if(input.equals("check") && object.hasFeature("container")){
            return ((Container)object.getFeature("container")).toString();
        }
        else if( input.equals("open")){
            return "You cant't open a "+object.getName()+"!";
        }
        else if(input.equals("useKey")){
            return "You cant't useKey on a "+object.getName()+"!";
        }

        return "";
    }

    @Override
    public String getName() {
        return NAME;
    }
}
