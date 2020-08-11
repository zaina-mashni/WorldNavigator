package com.WorldNavigator.commands;

import com.WorldNavigator.entities.MapInfo;
import com.WorldNavigator.entities.PlayerInfo;

import java.util.List;

public class Paper implements ICommand {
    @Override
    public String getName() {
        return "paper";
    }

    @Override
    public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
        checkArguments(player,map,splitCommand);
        if(splitCommand.get(0).equals("paper")){
            return "You both chose paper! play again.";
        }
        else if(splitCommand.get(0).equals("rock")){
            return "Paper beats rock. You won!";
        }
        return "Scissor beats paper. You lost!";
    }
}
