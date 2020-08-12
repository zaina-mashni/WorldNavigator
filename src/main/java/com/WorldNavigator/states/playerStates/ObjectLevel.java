package com.WorldNavigator.states.playerStates;

import com.WorldNavigator.commands.Finish;
import com.WorldNavigator.commands.ICommand;
import com.WorldNavigator.commands.Take;
import com.WorldNavigator.entities.Object;

import java.util.ArrayList;
import java.util.List;

public class ObjectLevel implements IPlayerState {
    private Object object;

    public ObjectLevel(Object object){
        checkIfNull("Object",object,"ObjectLevel");
        this.object=object;
    }

    public Object getObject(){
        return object;
    }

    @Override
    public List<ICommand> getAvailableCommands() {
        List<ICommand> availableCommands=new ArrayList<>();
        availableCommands.add(new Take());
        availableCommands.add(new Finish());
        return availableCommands;
    }

    @Override
    public String getName() {
        return "objectLevel";
    }

    @Override
    public String getDisplayName() {
        return "object";
    }

}
