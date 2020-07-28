package com.WorldNavigator.Commands;

import com.WorldNavigator.Entities.MapInfo;
import com.WorldNavigator.Entities.Object;
import com.WorldNavigator.Entities.PlayerInfo;
import com.WorldNavigator.Features.Container;
import com.WorldNavigator.States.PlayerStates.ObjectLevel;

import java.util.List;

public class Take implements ICommand {
    @Override
    public String getName() {
        return "take";
    }

    @Override
    public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
        if(!checkNumberOfInput(splitCommand,1)){
            return INVALID;
        }
        if(!player.getCurrentState().getName().equals("objectLevel")){
            throw new IllegalStateException("player not in objectLevel state while performing take command.");
        }
        Object object=((ObjectLevel) player.getCurrentState()).getObject();
        player.addToInventory(((Container)object.getFeature("container")).getItems());
        ((Container)object.getFeature("container")).removeAll();
        return "Items added to your inventory!";
    }
}
