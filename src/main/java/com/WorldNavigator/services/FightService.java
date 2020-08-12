package com.WorldNavigator.services;

import com.WorldNavigator.commands.ICommand;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.fights.IFightMode;
import com.WorldNavigator.entities.GameControl;
import com.WorldNavigator.fights.RockPaperScissorFight;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FightService {
  List<IFightMode> availableFights;

  public FightService() {
    availableFights = new ArrayList<>();
  }

  public IFightMode getFightInRoom(String worldName, int roomId) {
    checkIfNull("WorldName", worldName);
    return availableFights.stream()
        .filter(
            fightMode ->
                fightMode.getWorldName().equals(worldName) && fightMode.getRoomId() == roomId)
        .findFirst()
        .orElse(null);
  }

  public void addPlayerToFight(PlayerInfo player, ICommand choice, GameControl gameControl) {
    checkIfNull("Player", player);
    checkIfNull("Choice", choice);
    checkIfNull("GameControl", gameControl);
    IFightMode fight =
        getFightInRoom(gameControl.getWorldName(), player.getCurrentRoom().getRoomId());
    if (fight == null) {
      IFightMode fightMode = new RockPaperScissorFight();
      fightMode.setPlayerChoice(player, choice, gameControl);
      availableFights.add(fightMode);
    } else {
      fight.setPlayerChoice(player, choice, gameControl);
    }
  }

  public void endFightInRoom(String worldName, int roomId) {
    checkIfNull("WorldName", worldName);
    if (getFightInRoom(worldName, roomId) == null) {
      throw new IllegalArgumentException(
          "Trying to remove a fight that does not exist in class FightService.");
    }
    availableFights.removeIf(
        fightMode -> fightMode.getWorldName().equals(worldName) && fightMode.getRoomId() == roomId);
  }

  private void checkIfNull(String key, java.lang.Object value) {
    if (value == null) {
      throw new IllegalArgumentException(key + " can not be null in class FightService");
    }
  }
}
