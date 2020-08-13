package com.WorldNavigator.services;

import com.WorldNavigator.commands.ICommand;
import com.WorldNavigator.Direction;
import com.WorldNavigator.entities.MapInfo;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.fights.IFightMode;
import com.WorldNavigator.entities.GameControl;
import com.WorldNavigator.reply.DefaultReply;
import com.WorldNavigator.reply.ListGamesReply;
import com.WorldNavigator.utils.MapFileDecode;
import com.WorldNavigator.messages.ErrorMessages;
import com.WorldNavigator.messages.SuccessMessages;
import com.WorldNavigator.fights.RockPaperScissorFight;
import com.WorldNavigator.reply.GameReply;
import com.WorldNavigator.states.playerStates.FightLevel;
import com.WorldNavigator.states.roomStates.roomAvailabilityStates.IRoomAvailabilityState;
import com.WorldNavigator.views.GameInfoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class GameService {
  private static final String ok = "OK";
  private static final String error = "ERROR";

  private SimpMessagingTemplate webSocket;
  private CommandService commandService;
  private PlayerService playerService;
  private FightService fightService;
  private TimerService timerService;

  private Map<String, GameControl> availableGames;

  @Autowired
  public GameService(
      PlayerService playerService,
      CommandService commandService,
      SimpMessagingTemplate webSocket,
      FightService fightService,
      TimerService timerService) {
    this.playerService = playerService;
    this.commandService = commandService;
    this.webSocket = webSocket;
    this.availableGames = Collections.synchronizedMap(new HashMap<>());
    this.fightService = fightService;
    this.timerService = timerService;
  }

  public DefaultReply createNewGame(String adminName, String worldName, String mapFile) {
    try {
      checkIfNull("AdminName", adminName);
      checkIfNull("WorldName", worldName);
      checkIfNull("MapFile", mapFile);
      if (availableGames.containsKey(worldName)) {
        return setUpDefaultReply(ErrorMessages.gameExists, error);
      }
      if (playerService.isPlayerActive(adminName)) {
        availableGames.put(
            worldName,
            new MapFileDecode(worldName, playerService.getPlayer(adminName), this)
                .setMapFile(mapFile)
                .startDecode());
        joinGame(adminName, worldName);
        return setUpDefaultReply(SuccessMessages.createSuccess, ok);
      }
      return setUpDefaultReply(ErrorMessages.userNotActive, error);
    } catch (Exception e) {
      return throwInternalError(e);
    }
  }

  public DefaultReply joinGame(String playerName, String worldName) {
    try {
      checkIfNull("PlayerName", playerName);
      checkIfNull("WorldName", worldName);
      if (!availableGames.containsKey(worldName)) {
        return setUpDefaultReply(ErrorMessages.gameDoesNotExist, error);
      }
      if (!availableGames.get(worldName).isFull()) {
        if (playerService.isPlayerActive(playerName)) {
          playerService.playerJoinGame(playerName, worldName);
          playerService.playerJoinGame(playerName, worldName);
          availableGames.get(worldName).incrementJoinedPlayers();
          return setUpDefaultReply(SuccessMessages.joinSuccess, ok);
        }
        return setUpDefaultReply(ErrorMessages.userNotActive, error);
      }
      return setUpDefaultReply(ErrorMessages.gameFull, error);
    } catch (Exception e) {
      return throwInternalError(e);
    }
  }

  public DefaultReply unJoinGame(String playerName, String worldName) {
    try {
      checkIfNull("PlayerName", playerName);
      checkIfNull("WorldName", worldName);
      if (!availableGames.containsKey(worldName)) {
        return setUpDefaultReply(ErrorMessages.gameDoesNotExist, error);
      }
      if (!availableGames.get(worldName).isStarted()) {
        if (playerService.isPlayerActive(playerName)) {
          playerService.unJoinGame(playerName);
          availableGames.get(worldName).decrementJoinedPlayers();
          return setUpDefaultReply(SuccessMessages.unJoinSuccess, ok);
        }
        return setUpDefaultReply(ErrorMessages.userNotActive, error);
      }
      return setUpDefaultReply(ErrorMessages.gameStarted, error);
    } catch (Exception e) {
      return throwInternalError(e);
    }
  }

  public DefaultReply setUpDefaultReply(String message, String status) {
    DefaultReply reply = new DefaultReply();
    reply.setValue(message);
    reply.setStatus(status);
    return reply;
  }

  public GameReply setUpDefaultGameReply(PlayerInfo player, String status) {
    try {
      checkIfNull("Player", player);
      GameReply gameReply = new GameReply();
      gameReply.setAvailableCommands(
          commandService.convertToString(
              commandService.getAvailableCommands(player.getCurrentState())));
      if (!availableGames.get(player.getWorldName()).isStarted()) {
        gameReply.setTimeLeftInMinutes(
            availableGames.get(player.getWorldName()).getTimeRemaining());
      } else {
        gameReply.setTimeLeftInMinutes(
            timerService.getRemainingTimeInMinutesForGame(player.getWorldName()));
      }
      gameReply.setFacingDirection(Direction.values()[player.getFacingDirection()].name());
      gameReply.setInventory(player.getInventory().toString());
      gameReply.setFighting(false);
      gameReply.setPlaying(true);
      gameReply.setStatus(status);
      gameReply.setAdmin(availableGames.get(player.getWorldName()).getAdmin().getUsername().equals(player.getUsername()));
      gameReply.setWorldName(player.getWorldName());
      return gameReply;
    } catch (Exception e) {
      return endGameDueToInternalError(player, e);
    }
  }

  public GameReply setUpGameReplyForQuitPlayer(PlayerInfo player, String status) {
    try {
      checkIfNull("Player", player);
      GameReply gameReply = new GameReply();
      gameReply.setAvailableCommands(commandService.convertToString(new ArrayList<>()));
      gameReply.setTimeLeftInMinutes(0);
      gameReply.setFacingDirection(Direction.values()[player.getFacingDirection()].name());
      gameReply.setInventory(player.getInventory().toString());
      gameReply.setFighting(false);
      gameReply.setPlaying(false);
      gameReply.setAdmin(false);
      gameReply.setStatus(status);
      gameReply.setWorldName("");
      return gameReply;
    } catch (Exception e) {
      return endGameDueToInternalError(player, e);
    }
  }

  public GameReply executeCommand(String username, String input) {
    try {
      checkIfNull("Username", username);
      checkIfNull("Input", input);
      if (playerService.getPlayer(username).getCurrentState().getName().equals("fightLevel")) {
        return fight(username, input);
      }
      PlayerInfo player = playerService.getPlayer(username);
      MapInfo map = availableGames.get(player.getWorldName()).getMap();
      List<String> splitCommand = commandService.splitCommand(input);
      ICommand command = commandService.getCommand(player.getCurrentState(), splitCommand.get(0));
      if (command == null) {
        GameReply gameReply = setUpDefaultGameReply(player, ok);
        gameReply.setMessage("Invalid command!");
        return gameReply;
      }
      String message = command.execute(player, map, splitCommand);
      if (player.getCurrentRoom().getStatusState().getName().equals("exit")) {
        GameReply reply = setUpGameReplyForQuitPlayer(player, ok);
        reply.setMessage(message);
        Thread thread =
            new Thread(
                () -> {
                  try {
                    Thread.sleep(1000);
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                  endGame(player.getWorldName(), username + " won! Game has ended", ok);
                });
        thread.start();
        return reply;
      }
      GameReply gameReply = setUpDefaultGameReply(player, ok);
      gameReply.setMessage(message);
      return gameReply;
    } catch (Exception e) {
      return endGameDueToInternalError(playerService.getPlayer(username), e);
    }
  }

  public DefaultReply startGame(String username, String worldName) {
    try {
      checkIfNull("Username", username);
      checkIfNull("WorldName", worldName);
      DefaultReply reply = new DefaultReply();
      if (!availableGames.get(worldName).getAdmin().getUsername().equals(username)) {
        reply.setValue("No player other than the admin should be allowed to start the game.");
      }
      GameControl game = availableGames.get(worldName);
      playerService
          .getPlayersInWorld(worldName)
          .forEach(
              player ->
                  webSocket.convertAndSend(
                      "/socket/start/" + player.getUsername(),
                      setUpGameReplyToStartGame(player, game)));
      game.setStarted(true);
      timerService.startTimerForGame(worldName, game.getTimeRemaining(), this);
      return setUpDefaultReply(SuccessMessages.startSuccess, ok);
    } catch (Exception e) {
      return throwInternalError(e);
    }
  }

  public GameReply setUpGameReplyToStartGame(PlayerInfo player, GameControl game) {
    try {
      checkIfNull("Player", player);
      checkIfNull("Game", game);
      player.getInventory().removeAll();
      player.addToInventory(game.getInitialInventory().getItems());
      int spawnRoomIdx = (int) (Math.random() * game.getAvailableSpawnRooms().size());
      int facingDirection = (int) (Math.random() * 4);
      player.setCurrentRoom(game.assignSpawnRoom(spawnRoomIdx));
      player.getCurrentRoom().returnToOpenState();
      player.setFacingDirection(facingDirection);
      player.returnToRoomState();
      GameReply reply = setUpDefaultGameReply(player, ok);
      reply.setMessage("The game has started!");
      return reply;
    } catch (Exception e) {
      return endGameDueToInternalError(player, e);
    }
  }

  public GameReply setUpGameReplyToStartFight(PlayerInfo player) {
    try {
      checkIfNull("Player", player);
      GameReply reply = setUpDefaultGameReply(player, ok);
      reply.setMessage(
          "You both have the same amount of money. Now you have to battle it out with a game of rock paper scissors! You have 7 seconds to execute a command, if you don't or you enter an invalid command, a random one will be chosen for you.");
      reply.setFighting(true);
      return reply;
    } catch (Exception e) {
      return endGameDueToInternalError(player, e);
    }
  }

  public ListGamesReply getAvailableGames() {
    try {
      ListGamesReply reply = new ListGamesReply();
      reply.setAvailableGames(
          availableGames.entrySet().stream()
              .map(
                  game ->
                      new GameInfoView(
                          game.getValue().getWorldName(),
                          game.getValue().getPlayerCapacity(),
                          game.getValue().getPlayersJoined(),
                          game.getValue().getAdmin().getUsername(),
                          game.getValue().isStarted()))
              .collect(Collectors.toList()));
      reply.setStatus(ok);
      return reply;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      ListGamesReply reply = new ListGamesReply();
      reply.setAvailableGames(new ArrayList<>());
      reply.setStatus(error);
      return reply;
    }
  }

  public void onRoomFull(PlayerInfo player, IRoomAvailabilityState availabilityState) {
    try {
      checkIfNull("Player", player);
      checkIfNull("AvailabilityState", availabilityState);
      if (availabilityState.getName().equals("full")) {
        PlayerInfo oppositePlayer = playerService.getOppositePlayerInRoom(player);
        GameReply reply = new GameReply();
        setUpDefaultGameReply(oppositePlayer, ok);
        reply.setMessage(
            oppositePlayer
                .getCurrentRoom()
                .handlePostAvailabilityStateChangeInput(player, "forward"));
        webSocket.convertAndSend("/socket/start/" + oppositePlayer.getUsername(), reply);
        PlayerInfo winner =
            playerService.getRichestPlayer(playerService.getPlayersInSameRoomAsPlayer(player));
        if (winner == null) {
          playerService
              .getPlayersInSameRoomAsPlayer(player)
              .forEach(
                  playerInfo -> {
                    playerInfo.pushState(new FightLevel(new RockPaperScissorFight()));
                    webSocket.convertAndSend(
                        "/socket/start/" + playerInfo.getUsername(),
                        setUpGameReplyToStartFight(playerInfo));
                  });
        } else {
          PlayerInfo loser = playerService.getOppositePlayerInRoom(winner);
          reply = setUpDefaultGameReply(winner, ok);
          reply.setMessage("You have more money. You won!");
          reply.setFighting(false);
          webSocket.convertAndSend("/socket/start/" + winner.getUsername(), reply);
          reply = setUpDefaultGameReply(loser, ok);
          reply.setMessage("You have less money. You lost!");
          reply.setFighting(false);
          reply.setPlaying(false);
          webSocket.convertAndSend("/socket/start/" + loser.getUsername(), reply);
          loser.getCurrentRoom().handleAvailabilityStateChangeInput(loser,"leave");
          playerElimination(winner, loser);
        }
      }
    } catch (Exception e) {
      endGameDueToInternalError(player, e);
    }
  }

  public void playerElimination(PlayerInfo winner, PlayerInfo loser) {
    try {
      checkIfNull("Winner", winner);
      checkIfNull("Loser", loser);
      playerService.distributeWealth(winner, loser);
      availableGames.get(winner.getWorldName()).eliminatePlayer(loser);
      playerService.getPlayersInWorld(winner.getWorldName()).stream()
          .filter(player -> !player.getUsername().equals(loser.getUsername()))
          .forEach(
              playerInfo -> {
                GameReply reply = setUpDefaultGameReply(playerInfo, ok);
                reply.setMessage(
                    loser.getUsername()
                        + " has been eliminated! Some of their gold has been added to your inventory.");
                webSocket.convertAndSend("/socket/start/" + playerInfo.getUsername(), reply);
              });
    } catch (Exception e) {
      endGameDueToInternalError(winner, e);
    }
  }

  public synchronized GameReply fight(String playerUsername, String input) {
    try {
      checkIfNull("PlayerUsername", playerUsername);
      checkIfNull("Input", input);
      PlayerInfo player = playerService.getPlayer(playerUsername);
      PlayerInfo oppositePlayer =
          playerService.getOppositePlayerInRoom(playerService.getPlayer(playerUsername));
      GameReply reply;
      List<String> splitCommand = commandService.splitCommand(input);
      ICommand command =
          commandService.getCommand(
              playerService.getPlayer(playerUsername).getCurrentState(), splitCommand.get(0));
      fightService.addPlayerToFight(player, command, availableGames.get(player.getWorldName()));
      IFightMode fight =
          fightService.getFightInRoom(player.getWorldName(), player.getCurrentRoom().getRoomId());
      if (fight.isReady()) {
        PlayerInfo winner = fight.getWinningPlayer();
        if (winner == null) {
          reply = setUpDefaultGameReply(oppositePlayer, ok);
          reply.setFighting(true);
          reply.setMessage(fight.getResultOfFightForPlayer(oppositePlayer));
          webSocket.convertAndSend("/socket/start/" + oppositePlayer.getUsername(), reply);
          reply = setUpDefaultGameReply(player, ok);
          reply.setFighting(true);
          reply.setMessage(fight.getResultOfFightForPlayer(player));
          fightService.endFightInRoom(player.getWorldName(), player.getCurrentRoom().getRoomId());
          return reply;
        }
        PlayerInfo loser = fight.getLosingPlayer();
        winner.popState();
        loser.popState();
        loser.getCurrentRoom().handleAvailabilityStateChangeInput(loser,"leave");
        Thread thread = new Thread(() -> {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          playerElimination(winner, loser);
        });
        thread.start();
        if (oppositePlayer.getUsername().equals(loser.getUsername())) {
          reply = setUpGameReplyForQuitPlayer(oppositePlayer, ok);
          reply.setMessage(fight.getResultOfFightForPlayer(oppositePlayer));
          webSocket.convertAndSend("/socket/start/" + oppositePlayer.getUsername(), reply);
          reply = setUpDefaultGameReply(player, ok);
          reply.setMessage(fight.getResultOfFightForPlayer(player));
          return reply;
        }
        reply = setUpDefaultGameReply(oppositePlayer, ok);
        reply.setMessage(fight.getResultOfFightForPlayer(oppositePlayer));
        webSocket.convertAndSend("/socket/start/" + oppositePlayer.getUsername(), reply);
        reply = setUpGameReplyForQuitPlayer(player, ok);
        reply.setMessage(fight.getResultOfFightForPlayer(player));
        fightService.endFightInRoom(player.getWorldName(), player.getCurrentRoom().getRoomId());
        return reply;
      }
      reply = setUpDefaultGameReply(player, ok);
      reply.setMessage("Waiting for the other player to choose...");
      reply.setFighting(true);
      return reply;
    } catch (Exception e) {
      return endGameDueToInternalError(playerService.getPlayer(playerUsername), e);
    }
  }

  public GameReply quitPlayer(String username, String worldName) {
    try {
      checkIfNull("Username", username);
      checkIfNull("WorldName", worldName);
      PlayerInfo player = playerService.getPlayer(username);
      playerService.distributeGoldOfPlayer(player);
      availableGames.get(worldName).createChest(player);
      availableGames.get(worldName).eliminatePlayer(player);
      playerService.getPlayersInWorld(worldName).stream()
          .filter(playerInfo -> !playerInfo.getUsername().equals(player.getUsername()))
          .forEach(
              playerInfo -> {
                GameReply reply = setUpDefaultGameReply(playerInfo, ok);
                reply.setMessage(
                    username + " has quit! Some of their gold has been added to your inventory.");
                webSocket.convertAndSend("/socket/start/" + playerInfo.getUsername(), reply);
              });
      GameReply reply = setUpGameReplyForQuitPlayer(player, ok);
      reply.setMessage("You quit the game!");
      return reply;
    } catch (Exception e) {
      return endGameDueToInternalError(playerService.getPlayer(username), e);
    }
  }

  public void endGame(String worldName, String message, String status) {
    try {
      checkIfNull("WorldName", worldName);
      checkIfNull("Message", message);
      playerService
          .getPlayersInWorld(worldName)
          .forEach(
              player -> {
                player.setWorld("");
                GameReply reply = setUpGameReplyForQuitPlayer(player, status);
                reply.setMessage(message);
                webSocket.convertAndSend("/socket/start/" + player.getUsername(), reply);
              });
      availableGames.remove(worldName);
      timerService.endTimerForGame(worldName);
    } catch (Exception e) {
      throwInternalError(e);
    }
  }

  private GameReply endGameDueToInternalError(PlayerInfo player, Exception e) {
    GameReply gameReply = setUpGameReplyForQuitPlayer(player, error);
    System.out.println(e.getMessage());
    gameReply.setMessage(ErrorMessages.internalError);
    gameReply.setStatus(error);
    Thread thread =
        new Thread(
            () -> {
              try {
                Thread.sleep(1000);
              } catch (InterruptedException ex) {
                ex.printStackTrace();
              }
              endGame(player.getWorldName(), "Game ended due to internal error", error);
            });
    thread.start();
    return gameReply;
  }

  private DefaultReply throwInternalError(Exception e) {
    System.out.println(e.getMessage());
    return setUpDefaultReply(ErrorMessages.internalError,error);
  }

  private void checkIfNull(String key, Object value) {
    if (value == null) {
      throw new IllegalArgumentException(key + " is null in class GameService");
    }
  }
}
