package com.WorldNavigator.States.PlayerStates;

import com.WorldNavigator.Commands.Finish;
import com.WorldNavigator.Commands.ICommand;
import com.WorldNavigator.Commands.Take;
import com.WorldNavigator.Entities.Object;

import java.util.ArrayList;
import java.util.List;

public class ObjectLevel implements IPlayerState {
    private Object object;

    public ObjectLevel(Object object){
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
}
