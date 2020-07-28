package com.WorldNavigator.Commands;

import com.WorldNavigator.Entities.MapInfo;
import com.WorldNavigator.Entities.PlayerInfo;

import java.util.List;

public class Scissors implements ICommand {
    @Override
    public String getName() {
        return "scissor";
    }

    @Override
    public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
        if (splitCommand.get(0).equals("scissor")) {
            return "You both chose scissor! play again.";
        } else if (splitCommand.get(0).equals("rock")) {
            return "Rock beats scissor. You lost!";
        }
        return "Scissor beats paper. You won!";
    }
}
