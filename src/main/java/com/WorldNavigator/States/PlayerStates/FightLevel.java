package com.WorldNavigator.States.PlayerStates;

import com.WorldNavigator.Commands.*;
import com.WorldNavigator.Features.IFightMode;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class FightLevel implements IPlayerState {

    IFightMode fightMode;

    public FightLevel(IFightMode fightMode){
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
}
