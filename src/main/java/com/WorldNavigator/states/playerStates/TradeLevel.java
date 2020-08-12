package com.WorldNavigator.states.playerStates;

import com.WorldNavigator.commands.*;
import com.WorldNavigator.entities.Object;

import java.util.ArrayList;
import java.util.List;

public class TradeLevel implements IPlayerState {
    Object object;

    public TradeLevel(Object tradeObject) {
        checkIfNull("TradeObject",tradeObject,"TradeLevel");
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

    @Override
    public String getDisplayName() {
        return "seller";
    }

}
