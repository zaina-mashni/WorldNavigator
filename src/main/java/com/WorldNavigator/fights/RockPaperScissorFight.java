package com.WorldNavigator.fights;

import com.WorldNavigator.commands.ICommand;
import com.WorldNavigator.commands.Paper;
import com.WorldNavigator.commands.Rock;
import com.WorldNavigator.commands.Scissors;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.GameControl;
import com.WorldNavigator.utils.Pair;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RockPaperScissorFight implements IFightMode {
  GameControl gameControl;
  Pair<PlayerInfo, ICommand> firstPlayerChoice;
  Pair<PlayerInfo, ICommand> secondPlayerChoice;
  boolean ready;

  public RockPaperScissorFight() {
    ready = false;
  }

  @Override
  public String getResultOfFightForPlayer(PlayerInfo player) {
    checkIfNull("Player", player);
    if (firstPlayerChoice == null || secondPlayerChoice == null) {
      throw new IllegalStateException(
          "PlayerChoice must be set before getting it in class RockPaperScissorFight");
    }
    if (firstPlayerChoice.getKey().getUsername().equals(player.getUsername())) {
      List<String> splitCommandForFirstPlayer =
          Collections.singletonList(secondPlayerChoice.getValue().getName());
      return firstPlayerChoice
          .getValue()
          .execute(firstPlayerChoice.getKey(), gameControl.getMap(), splitCommandForFirstPlayer);
    } else if (secondPlayerChoice.getKey().getUsername().equals(player.getUsername())) {
      List<String> splitCommandForSecondPlayer =
          Collections.singletonList(firstPlayerChoice.getValue().getName());
      return secondPlayerChoice
          .getValue()
          .execute(secondPlayerChoice.getKey(), gameControl.getMap(), splitCommandForSecondPlayer);
    }
    throw new IllegalArgumentException("Player not found in class RockPaperScissorFight");
  }

  @Override
  public int getRoomId() {
    if (firstPlayerChoice == null) {
      throw new IllegalStateException(
          "getRoomId in class RockPaperScissorFight is called before setting first player.");
    }
    return firstPlayerChoice.getKey().getCurrentRoom().getRoomId();
  }

  @Override
  public String getWorldName() {
    if (gameControl == null) {
      throw new IllegalStateException(
          "getWorldName in class RockPaperScissorFight is called before setting first player.");
    }
    return gameControl.getWorldName();
  }

  @Override
  public PlayerInfo getWinningPlayer() {
    String resultForFirstPlayer = getResultOfFightForPlayer(firstPlayerChoice.getKey());
    String resultForSecondPlayer = getResultOfFightForPlayer(secondPlayerChoice.getKey());
    if (resultForFirstPlayer.matches(".*won.*")) {
      return firstPlayerChoice.getKey();
    } else if (resultForSecondPlayer.matches(".*won.*")) {
      return secondPlayerChoice.getKey();
    }
    return null;
  }

  @Override
  public PlayerInfo getLosingPlayer() {
    String resultForFirstPlayer = getResultOfFightForPlayer(firstPlayerChoice.getKey());
    String resultForSecondPlayer = getResultOfFightForPlayer(secondPlayerChoice.getKey());
    if (resultForFirstPlayer.matches(".*lost.*")) {
      return firstPlayerChoice.getKey();
    } else if (resultForSecondPlayer.matches(".*lost.*")) {
      return secondPlayerChoice.getKey();
    }
    return null;
  }

  @Override
  public List<ICommand> getAvailableCommands() {
    List<ICommand> availableCommands = new ArrayList<>();
    availableCommands.add(new Rock());
    availableCommands.add(new Paper());
    availableCommands.add(new Scissors());
    return availableCommands;
  }

  @Override
  public void setPlayerChoice(PlayerInfo player, ICommand choice, GameControl gameControl) {
    checkIfNull("Player", player);
    checkIfNull("Choice", choice);
    checkIfNull("GameControl", gameControl);
    if (firstPlayerChoice == null) {
      firstPlayerChoice = new Pair<>(player, choice);
      this.gameControl = gameControl;
    } else if (secondPlayerChoice == null) {
      secondPlayerChoice = new Pair<>(player, choice);
      if (!this.gameControl.getWorldName().equals(gameControl.getWorldName())) {
        throw new IllegalArgumentException(
            "Players from different games can not fight together in class RockPaperScissorFight.");
      }
    } else {
      throw new IllegalArgumentException(
          "RockPaperScissorFight should only be played by two players at a time.");
    }
    if (secondPlayerChoice != null) {
      ready = true;
    }
  }

  @Override
  public boolean isReady() {
    return ready;
  }

  private void checkIfNull(String key, java.lang.Object value) {
    if (value == null) {
      throw new IllegalArgumentException(key + " can not be null in class RockPaperScissorFight");
    }
  }
}
