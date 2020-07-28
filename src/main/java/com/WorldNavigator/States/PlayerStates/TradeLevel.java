package com.WorldNavigator.States.PlayerStates;

import com.WorldNavigator.Commands.*;
import com.WorldNavigator.Entities.Object;

import java.util.ArrayList;
import java.util.List;

public class TradeLevel implements IPlayerState {
    Object object;
    public TradeLevel(Object tradeObject) {
        this.object=tradeObject;
    }

    public Object getObject(){
        return object;
    }

    @Override
    public List<ICommand> getAvailableCommands() {
        List<ICommand> availableCommands=new ArrayList<>();
        availableCommands.add(new Buy());
        availableCommands.add(new Sell());
        availableCommands.add(new ListSellerItems());
        availableCommands.add(new Finish());
        return availableCommands;
    }

    @Override
    public String getName() {
        return "tradeLevel";
    }
}
