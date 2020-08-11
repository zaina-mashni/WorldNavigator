package com.WorldNavigator.commands;

import com.WorldNavigator.entities.MapInfo;
import com.WorldNavigator.entities.PlayerInfo;

import java.util.List;

public class Rock implements ICommand {
    @Override
    public String getName() {
        return "rock";
    }

    @Override
    public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
        checkArguments(player,map,splitCommand);
        if(splitCommand.get(0).equals("rock")){
            return "You both chose rock! play again.";
        }
        else if(splitCommand.get(0).equals("paper")){
            return "Paper beats rock. You lost!";
        }
        return "Rock beats scissor. You won!";
    }
}
