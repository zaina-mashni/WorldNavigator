package com.WorldNavigator.Services;

import com.WorldNavigator.Entities.PlayerInfo;
import com.WorldNavigator.Messages.ErrorMessages;
import com.WorldNavigator.Messages.SuccessMessages;
import com.WorldNavigator.Model.PlayerModel;
import com.WorldNavigator.Repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class PlayerService {

  private Map<String, PlayerInfo> activePlayers;
  private PlayerRepository playerRepository;

  @Autowired
  public PlayerService(PlayerRepository playerRepository) {
    activePlayers = Collections.synchronizedMap(new HashMap<>());
    this.playerRepository = playerRepository;
  }

  public String playerLogin(String username, String password) {
    PlayerModel playerModel = playerRepository.findByUsernameIgnoreCase(username).orElse(null);
    if (playerModel == null || !playerModel.getPassword().equals(password)) {
      return ErrorMessages.wrongUserOrPass;
    }
    if (!activePlayers.containsKey(username)) {
      activePlayers.put(username, new PlayerInfo(username));
      return SuccessMessages.loginSuccess;
    }
    return SuccessMessages.loginSuccess;
  }

  public String playerLogout(String username) {
    if (!activePlayers.containsKey(username)) {
      return "player is not logged in";
    }
    activePlayers.remove(username);
    return "logged out successfully";
  }

  public String registerPlayer(String username, String password) {
    PlayerModel playerModel = playerRepository.findByUsernameIgnoreCase(username).orElse(null);
    if (playerModel != null) {
      return ErrorMessages.userExists;
    }
    playerModel = new PlayerModel();
    playerModel.setUsername(username);
    playerModel.setPassword(password);
    playerRepository.save(playerModel);
    return SuccessMessages.registrationSuccess;
  }

  public void playerJoinGame(String username, String worldName) {
    activePlayers.get(username).setWorld(worldName);
  }

  public PlayerInfo getPlayer(String username) {
    if (activePlayers.containsKey(username)) {
      return activePlayers.get(username);
    }
    return null;
  }

  public boolean isPlayerActive(String username) {
    return activePlayers.containsKey(username);
  }

  public List<PlayerInfo> getPlayersInWorld(String worldName) {
    return activePlayers.values().stream()
        .filter(player -> player.getWorldName().equals(worldName))
        .collect(Collectors.toList());
  }

  public PlayerInfo getRichestPlayer(List<PlayerInfo> players) {
    int mxWorth = 0;
    List<PlayerInfo> playersWithMaxWorth = new ArrayList<>();
    for (PlayerInfo player : players) {
      mxWorth = Math.max(mxWorth, player.getWorth());
    }
    int finalMxWorth = mxWorth;
    players.forEach(
        player -> {
          if (player.getWorth() == finalMxWorth) {
            playersWithMaxWorth.add(player);
          }
        });
    if (playersWithMaxWorth.size() > 1) {
      return null;
    }
    return playersWithMaxWorth.get(0);
  }

  public PlayerInfo getOppositePlayerInRoom(PlayerInfo player) {
    return getPlayersInSameRoomAsPlayer(player).stream()
        .filter(playerInfo -> !playerInfo.getUsername().equals(player.getUsername()))
        .findFirst()
        .orElse(null);
  }

  public List<PlayerInfo> getPlayersInSameRoomAsPlayer(PlayerInfo player) {
    return getPlayersInWorld(player.getWorldName()).stream()
        .filter(
            playerInfo ->
                playerInfo.getCurrentRoom().getRoomIndex()
                    == player.getCurrentRoom().getRoomIndex())
        .collect(Collectors.toList());
  }

  public void distributeGold(PlayerInfo loser) {
    int gold = loser.getGoldAmount() / (getPlayersInWorld(loser.getWorldName()).size() - 1);
    loser.getInventory().replaceItem("gold", 0);
    getPlayersInWorld(loser.getWorldName())
        .forEach(
            player -> {
              if (!player.getUsername().equals(loser.getUsername())) {
                player.updateGoldAmount(gold);
              }
            });
  }

  public void distributeWealth(PlayerInfo winner, PlayerInfo loser) {
    distributeGold(loser);
    winner.getInventory().addItems(loser.getInventory().getItems());
    loser.getInventory().removeAll();
  }
}
