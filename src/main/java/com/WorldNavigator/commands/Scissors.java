package com.WorldNavigator.commands;

import com.WorldNavigator.entities.MapInfo;
import com.WorldNavigator.entities.PlayerInfo;

import java.util.List;

public class Scissors implements ICommand {
  @Override
  public String getName() {
    return "scissor";
  }

  @Override
  public String execute(PlayerInfo player, MapInfo map, List<String> splitCommand) {
    checkArguments(player, map, splitCommand, "Scissor");
    if (splitCommand.get(0).equals("scissor")) {
      return "You both chose scissor! play again.";
    } else if (splitCommand.get(0).equals("rock")) {
      return "Rock beats scissor. You lost!";
    }
    return "Scissor beats paper. You won!";
  }
}
