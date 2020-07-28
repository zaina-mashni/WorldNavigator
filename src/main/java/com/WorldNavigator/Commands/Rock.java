package com.WorldNavigator.Commands;

import com.WorldNavigator.Entities.MapInfo;
import com.WorldNavigator.Entities.PlayerInfo;

import java.util.List;

public class Rock implements ICommand {
    @Override
    public String getName() {
        return "rock";
    }

    @Override
    public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
        if(splitCommand.get(0).equals("rock")){
            return "You both chose rock! play again.";
        }
        else if(splitCommand.get(0).equals("paper")){
            return "Paper beats rock. You lost!";
        }
        return "Rock beats scissor. You won!";
    }
}
