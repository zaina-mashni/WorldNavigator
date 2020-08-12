package com.WorldNavigator.services;

import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.messages.ErrorMessages;
import com.WorldNavigator.messages.SuccessMessages;
import com.WorldNavigator.model.PlayerModel;
import com.WorldNavigator.reply.DefaultReply;
import com.WorldNavigator.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class PlayerService {
  private static final String ok = "OK";
  private static final String error = "ERROR";

  private Map<String, PlayerInfo> activePlayers;
  private PlayerRepository playerRepository;

  @Autowired
  public PlayerService(PlayerRepository playerRepository) {
    activePlayers = Collections.synchronizedMap(new HashMap<>());
    this.playerRepository = playerRepository;
  }

  public DefaultReply playerLogin(String username, String password) {
    try {
      checkIfNull("Username", username);
      checkIfNull("Password", password);
      PlayerModel playerModel = playerRepository.findByUsernameIgnoreCase(username).orElse(null);
      if (playerModel == null || !playerModel.getPassword().equals(password)) {
        return setUpDefaultReply(ErrorMessages.wrongUserOrPass,error);
      }
      if (!activePlayers.containsKey(username)) {
        activePlayers.put(username, new PlayerInfo(username));
        return setUpDefaultReply(SuccessMessages.loginSuccess,ok);
      }
      return setUpDefaultReply(ErrorMessages.userAlreadyActive,error);
    } catch (Exception e) {
      return throwInternalError(e);
    }
  }

  public DefaultReply playerLogout(String username) {
    try {
      checkIfNull("Username", username);
      if (!activePlayers.containsKey(username)) {
        return setUpDefaultReply(ErrorMessages.userNotActive,error);
      }
      activePlayers.remove(username);
      return setUpDefaultReply(SuccessMessages.loginOutSuccess,ok);
    } catch (Exception e) {
      return throwInternalError(e);
    }
  }

  public DefaultReply registerPlayer(String username, String password) {
    try {
      checkIfNull("Username", username);
      checkIfNull("Password", password);
      PlayerModel playerModel = playerRepository.findByUsernameIgnoreCase(username).orElse(null);
      if (playerModel != null) {
        return setUpDefaultReply(ErrorMessages.userExists,error);
      }
      playerModel = new PlayerModel();
      playerModel.setUsername(username);
      playerModel.setPassword(password);
      playerRepository.save(playerModel);
      return setUpDefaultReply(SuccessMessages.registrationSuccess,ok);
    } catch (Exception e) {
      return throwInternalError(e);
    }
  }

  public void playerJoinGame(String username, String worldName) {
    checkIfNull("Username", username);
    checkIfNull("WorldName", worldName);
    if (!activePlayers.containsKey(username)) {
      throw new IllegalStateException("Player can not join game if they are not active");
    }
    activePlayers.get(username).setWorld(worldName);
  }

  public PlayerInfo getPlayer(String username) {
    if (activePlayers.containsKey(username)) {
      return activePlayers.get(username);
    }
    throw new IllegalStateException("You have to set player before getting them.");
  }

  public boolean isPlayerActive(String username) {
    checkIfNull("Username", username);
    return activePlayers.containsKey(username);
  }

  public List<PlayerInfo> getPlayersInWorld(String worldName) {
    checkIfNull("WorldName", worldName);
    return activePlayers.values().stream()
        .filter(player -> player.getWorldName().equals(worldName))
        .collect(Collectors.toList());
  }

  public PlayerInfo getRichestPlayer(List<PlayerInfo> players) {
    checkIfNull("Players", players);
    int mxWorth = 0;
    List<PlayerInfo> playersWithMaxWorth = new ArrayList<>();
    for (PlayerInfo player : players) {
      mxWorth = Math.max(mxWorth, player.convertInventoryToGoldAmount());
    }
    int finalMxWorth = mxWorth;
    players.forEach(
        player -> {
          if (player.convertInventoryToGoldAmount() == finalMxWorth) {
            playersWithMaxWorth.add(player);
          }
        });
    if (playersWithMaxWorth.size() > 1) {
      return null;
    }
    return playersWithMaxWorth.get(0);
  }

  public PlayerInfo getOppositePlayerInRoom(PlayerInfo player) {
    checkIfNull("Player", player);
    return getPlayersInSameRoomAsPlayer(player).stream()
        .filter(playerInfo -> !playerInfo.getUsername().equals(player.getUsername()))
        .findFirst()
        .orElse(null);
  }

  public List<PlayerInfo> getPlayersInSameRoomAsPlayer(PlayerInfo player) {
    checkIfNull("Player", player);
    return getPlayersInWorld(player.getWorldName()).stream()
        .filter(
            playerInfo ->
                playerInfo.getCurrentRoom().getRoomId() == player.getCurrentRoom().getRoomId())
        .collect(Collectors.toList());
  }

  public void distributeGoldOfPlayer(PlayerInfo player) {
    checkIfNull("Player", player);
    int gold = 0;
    if (getPlayersInWorld(player.getWorldName()).size() != 1) {
      gold = player.getGoldAmount() / (getPlayersInWorld(player.getWorldName()).size() - 1);
    }
    player.getInventory().replaceItem("gold", 0);
    int goldPerPlayer = gold;
    getPlayersInWorld(player.getWorldName())
        .forEach(
            playerInfo -> {
              if (!playerInfo.getUsername().equals(player.getUsername())) {
                playerInfo.updateGoldAmount(goldPerPlayer);
              }
            });
  }

  public void distributeWealth(PlayerInfo winner, PlayerInfo loser) {
    checkIfNull("Winner", winner);
    checkIfNull("Loser", loser);
    distributeGoldOfPlayer(loser);
    winner.getInventory().addItems(loser.getInventory().getItems());
    loser.getInventory().removeAll();
  }

  public void unJoinGame(String username) {
    checkIfNull("Username", username);
    activePlayers.get(username).setWorld("");
  }

  public DefaultReply setUpDefaultReply(String message, String status) {
    DefaultReply reply = new DefaultReply();
    reply.setValue(message);
    reply.setStatus(status);
    return reply;
  }

  private DefaultReply throwInternalError(Exception e) {
    System.out.println(e.getMessage());
    return setUpDefaultReply(ErrorMessages.internalError,error);
  }

  private void checkIfNull(String key, Object value) {
    if (value == null) {
      throw new IllegalArgumentException(key + " is null in class PlayerService");
    }
  }
}
