package com.WorldNavigator.states.playerStates;

import com.WorldNavigator.commands.*;
import com.WorldNavigator.fights.IFightMode;

import java.util.List;

public class FightLevel implements IPlayerState {

    IFightMode fightMode;

    public FightLevel(IFightMode fightMode){
        checkIfNull("FightMode",fightMode,"FightLevel");
        this.fightMode = fightMode;
    }

    @Override
    public List<ICommand> getAvailableCommands() {
        return fightMode.getAvailableCommands();
    }

    @Override
    public String getName() {
        return "fightLevel";
    }

    @Override
    public String getDisplayName() {
        return "opponent";
    }
}
