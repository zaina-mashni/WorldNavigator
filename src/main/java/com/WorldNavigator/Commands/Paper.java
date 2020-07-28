package com.WorldNavigator.Commands;

import com.WorldNavigator.Entities.MapInfo;
import com.WorldNavigator.Entities.PlayerInfo;

import java.util.List;

public class Paper implements ICommand {
    @Override
    public String getName() {
        return "paper";
    }

    @Override
    public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
        if(splitCommand.get(0).equals("paper")){
            return "You both chose paper! play again.";
        }
        else if(splitCommand.get(0).equals("rock")){
            return "Paper beats rock. You won!";
        }
        return "Scissor beats paper. You lost!";
    }
}
