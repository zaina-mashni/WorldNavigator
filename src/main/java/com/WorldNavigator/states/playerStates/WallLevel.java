package com.WorldNavigator.states.playerStates;

import com.WorldNavigator.commands.*;

import java.util.ArrayList;
import java.util.List;

public class WallLevel implements IPlayerState {
    @Override
    public List<ICommand> getAvailableCommands() {
        List<ICommand> availableCommands=new ArrayList<>();
        availableCommands.add(new Trade());
        availableCommands.add(new Check());
        availableCommands.add(new Open());
        availableCommands.add(new UseKey());
        availableCommands.add(new Look());
        availableCommands.add(new Finish());
        return availableCommands;
    }

    @Override
    public String getName() {
        return "wallLevel";
    }

    @Override
    public String getDisplayName() {
        return "wall";
    }
}
