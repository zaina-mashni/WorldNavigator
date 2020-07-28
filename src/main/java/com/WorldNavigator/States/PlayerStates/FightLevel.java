package com.WorldNavigator.States.PlayerStates;

import com.WorldNavigator.Commands.*;

import java.util.ArrayList;
import java.util.List;

public class FightLevel implements IPlayerState {

    @Override
    public List<ICommand> getAvailableCommands() {
        List<ICommand> availableCommands=new ArrayList<>();
        availableCommands.add(new Rock());
        availableCommands.add(new Paper());
        availableCommands.add(new Scissors());
        return availableCommands;
    }

    @Override
    public String getName() {
        return "fightLevel";
    }
}
