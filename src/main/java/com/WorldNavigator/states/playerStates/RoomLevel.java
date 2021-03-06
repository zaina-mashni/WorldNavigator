package com.WorldNavigator.states.playerStates;

import com.WorldNavigator.commands.*;

import java.util.ArrayList;
import java.util.List;

public class RoomLevel implements IPlayerState {
    @Override
    public List<ICommand> getAvailableCommands() {
        List<ICommand> availableCommands=new ArrayList<>();
        availableCommands.add(new Backward());
        availableCommands.add(new Forward());
        availableCommands.add(new Left());
        availableCommands.add(new Right());
        availableCommands.add(new Look());
        availableCommands.add(new UseFlashlight());
        availableCommands.add(new UseSwitch());
        return availableCommands;
    }
    @Override
    public String getName() {
        return "roomLevel";
    }

    @Override
    public String getDisplayName() {
        return "room";
    }


}
