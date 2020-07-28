package com.WorldNavigator.Commands;

import com.WorldNavigator.Entities.MapInfo;
import com.WorldNavigator.Entities.PlayerInfo;

import java.util.List;

public class UseFlashlight implements ICommand {
    @Override
    public String getName() {
        return "useFlashlight";
    }

    @Override
    public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
        if(!checkNumberOfInput(splitCommand,1)){
            return INVALID;
        }
        if(!player.hasItemInInventory("flashlight")){
            return "You don't have a flashlight in your inventory!";
        }

        player.getItemFromInventory("flashlight").handleStateChangeInput(getName());
        return player.getItemFromInventory("flashlight").handleStateSpecificInput(getName());
    }
}
