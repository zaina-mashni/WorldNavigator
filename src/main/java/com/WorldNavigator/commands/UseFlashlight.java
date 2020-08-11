package com.WorldNavigator.commands;

import com.WorldNavigator.entities.MapInfo;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.messages.ErrorMessages;

import java.util.List;

public class UseFlashlight implements ICommand {
    @Override
    public String getName() {
        return "useFlashlight";
    }

    @Override
    public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
        checkArguments(player,map,splitCommand);
        if(!checkNumberOfInput(splitCommand,1)){
            return ErrorMessages.invalidInput;
        }
        if(!player.hasItemInInventory("flashlight")){
            return "You don't have a flashlight in your inventory!";
        }

        player.getItemFromInventory("flashlight").handleStateChangeInput(getName());
        return player.getItemFromInventory("flashlight").handleStateSpecificInput(getName());
    }
}
