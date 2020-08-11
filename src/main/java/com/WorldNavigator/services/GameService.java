package com.WorldNavigator.services;

import com.WorldNavigator.commands.ICommand;
import com.WorldNavigator.Direction;
import com.WorldNavigator.entities.MapInfo;
import com.WorldNavigator.entities.PlayerInfo;
import com.WorldNavigator.fights.IFightMode;
import com.WorldNavigator.GameControl;
import com.WorldNavigator.reply.DefaultReply;
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

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GameService {

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

  public String createNewGame(String adminName, String worldName, String mapFile) {
    if (availableGames.containsKey(worldName)) {
      return ErrorMessages.gameExists;
    }
    if (playerService.isPlayerActive(adminName)) {
      try{
      availableGames.put(
          worldName,
          new MapFileDecode(worldName, playerService.getPlayer(adminName), this)
              .setMapFile(mapFile)
              .startDecode());
      joinGame(adminName, worldName);
      return SuccessMessages.createSuccess;
      }
      catch (IOException e){
        System.out.println(e.getMessage());
        return ErrorMessages.internalError;
      }
    }
    return ErrorMessages.userNotActive;
  }

  public String joinGame(String playerName, String worldName) {
    if (!availableGames.containsKey(worldName)) {
      return ErrorMessages.gameDoesNotExist;
    }
    if (!availableGames.get(worldName).isFull()) {
      if (playerService.isPlayerActive(playerName)) {
        playerService.playerJoinGame(playerName, worldName);
        playerService.playerJoinGame(playerName, worldName);
        availableGames.get(worldName).incrementJoinedPlayers();
        System.out.println(availableGames.get(worldName).getPlayersJoined());
        return SuccessMessages.joinSuccess;
      }
      return ErrorMessages.userNotActive;
    }
    return ErrorMessages.gameFull;
  }

  public String unJoinGame(String playerName, String worldName) {
    if (!availableGames.containsKey(worldName)) {
      return ErrorMessages.gameDoesNotExist;
    }
    if (!availableGames.get(worldName).isStarted()) {
      if (playerService.isPlayerActive(playerName)) {
        playerService.unJoinGame(playerName);
        availableGames.get(worldName).decrementJoinedPlayers();
        System.out.println(availableGames.get(worldName).getPlayersJoined());
        return SuccessMessages.unJoinSuccess;
      }
      return ErrorMessages.userNotActive;
    }
    return ErrorMessages.gameStarted;
  }

  public GameReply setUpDefaultGameReply(PlayerInfo player) {
    GameReply gameReply = new GameReply();
    gameReply.setAvailableCommands(
        commandService.convertToString(
            commandService.getAvailableCommands(player.getCurrentState())));
    if(!availableGames.get(player.getWorldName()).isStarted()){
      gameReply.setTimeLeftInMinutes(availableGames.get(player.getWorldName()).getTimeRemaining());
    } else{
      gameReply.setTimeLeftInMinutes(timerService.getRemainingTimeInMinutesForGame(player.getWorldName()));
    }
    gameReply.setFacingDirection(Direction.values()[player.getFacingDirection()].name());
    gameReply.setInventory(player.getInventory().toString());
    gameReply.setFighting(false);
    gameReply.setPlaying(true);
    return gameReply;
  }

  public GameReply setUpGameReplyForQuitPlayer(PlayerInfo player) {
    GameReply gameReply = new GameReply();
    gameReply.setAvailableCommands(commandService.convertToString(new ArrayList<>()));
    gameReply.setTimeLeftInMinutes(0);
    gameReply.setFacingDirection(Direction.values()[player.getFacingDirection()].name());
    gameReply.setInventory(player.getInventory().toString());
    gameReply.setFighting(false);
    gameReply.setPlaying(false);
    return gameReply;
  }

  public GameReply executeCommand(String username, String input) {
    if (playerService.getPlayer(username).getCurrentState().getName().equals("fightLevel")) {
      return fight(username, input);
    }
    PlayerInfo player = playerService.getPlayer(username);
    MapInfo map = availableGames.get(player.getWorldName()).getMap();
    List<String> splitCommand = commandService.splitCommand(input);
    ICommand command = commandService.getCommand(player.getCurrentState(), splitCommand.get(0));
    if (command == null) {
      GameReply gameReply = setUpDefaultGameReply(player);
      gameReply.setMessage("Invalid command!");
      return gameReply;
    }
    String message = command.execute(player, map, splitCommand);
    if (player.getCurrentRoom().getStatusState().getName().equals("exit")) {
      GameReply reply = setUpGameReplyForQuitPlayer(player);
      reply.setMessage(message);
      Thread thread =
          new Thread(
              () -> {
                try {
                  Thread.sleep(1000);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
                endGame(player.getWorldName(), username + " won! Game has ended");
              });
      thread.start();
      return reply;
    }
    GameReply gameReply = setUpDefaultGameReply(player);
    gameReply.setMessage(message);
    return gameReply;
  }

  public DefaultReply startGame(String username, String worldName) {
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
    reply.setValue(SuccessMessages.startSuccess);
    return reply;
  }

  public GameReply setUpGameReplyToStartGame(PlayerInfo player, GameControl game) {
    player.getInventory().removeAll();
    player.addToInventory(game.getInitialInventory().getItems());
    int spawnRoomIdx = (int) (Math.random() * game.getAvailableSpawnRooms().size());
    int facingDirection = (int) (Math.random() * 4);
    player.setCurrentRoom(game.assignSpawnRoom(spawnRoomIdx));
    player.getCurrentRoom().returnToEmptyState();
    player.setFacingDirection(facingDirection);
    player.returnToRoomState();
    GameReply reply = setUpDefaultGameReply(player);
    reply.setMessage("The game has started!");
    return reply;
  }

  public GameReply setUpGameReplyToStartFight(PlayerInfo player) {
    GameReply reply = setUpDefaultGameReply(player);
    reply.setMessage(
        "You both have the same amount of money. Now you have to battle it out with a game of rock paper scissors! You have 7 seconds to execute a command, if you don't or you enter an invalid command, a random one will be chosen for you.");
    reply.setFighting(true);
    return reply;
  }

  public List<GameInfoView> getAvailableGames() {
    return availableGames.entrySet().stream()
        //   .filter(game -> !game.getValue().isStarted())
        .map(
            game ->
                new GameInfoView(
                    game.getValue().getWorldName(),
                    game.getValue().getPlayerCapacity(),
                    game.getValue().getPlayersJoined(),
                    game.getValue().getAdmin().getUsername(),
                    game.getValue().isStarted()))
        .collect(Collectors.toList());
  }

  public void onRoomFull(PlayerInfo player, IRoomAvailabilityState availabilityState) {
    if (availabilityState.getName().equals("full")) {
      PlayerInfo oppositePlayer = playerService.getOppositePlayerInRoom(player);
      GameReply reply = new GameReply();
      setUpDefaultGameReply(oppositePlayer);
      reply.setMessage(
          oppositePlayer.getCurrentRoom().handleAvailabilityStateSpecificInput(player, "forward"));
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
        reply = setUpDefaultGameReply(winner);
        reply.setMessage("You have more money. You won!");
        reply.setFighting(false);
        webSocket.convertAndSend("/socket/start/" + winner.getUsername(), reply);
        reply = setUpDefaultGameReply(loser);
        reply.setMessage("You have less money. You lost!");
        reply.setFighting(false);
        reply.setPlaying(false);
        webSocket.convertAndSend("/socket/start/" + loser.getUsername(), reply);
        playerElimination(winner, loser);
      }
    }
  }

  public void playerElimination(PlayerInfo winner, PlayerInfo loser) {
    playerService.distributeWealth(winner, loser);
    availableGames.get(winner.getWorldName()).eliminatePlayer(loser);
    playerService.getPlayersInWorld(winner.getWorldName()).stream()
        .filter(player -> !player.getUsername().equals(loser.getUsername()))
        .forEach(
            playerInfo -> {
              GameReply reply = setUpDefaultGameReply(playerInfo);
              reply.setMessage(
                  loser.getUsername()
                      + " has been eliminated! Some of their gold has been added to your inventory.");
              webSocket.convertAndSend("/socket/start/" + playerInfo.getUsername(), reply);
            });
  }

  public synchronized GameReply fight(String playerUsername, String input) {
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
        reply = setUpDefaultGameReply(oppositePlayer);
        reply.setFighting(true);
        reply.setMessage(fight.getResultOfFightForPlayer(oppositePlayer));
        webSocket.convertAndSend("/socket/start/" + oppositePlayer.getUsername(), reply);
        reply = setUpDefaultGameReply(player);
        reply.setFighting(true);
        reply.setMessage(fight.getResultOfFightForPlayer(player));
        fightService.endFightInRoom(player.getWorldName(), player.getCurrentRoom().getRoomId());
        return reply;
      }
      PlayerInfo loser = fight.getLosingPlayer();
      winner.popState();
      loser.popState();
      Thread thread = new Thread(() -> playerElimination(winner, loser));
      thread.start();
      if (oppositePlayer.getUsername().equals(loser.getUsername())) {
        reply = setUpGameReplyForQuitPlayer(oppositePlayer);
        reply.setMessage(fight.getResultOfFightForPlayer(oppositePlayer));
        webSocket.convertAndSend("/socket/start/" + oppositePlayer.getUsername(), reply);
        reply = setUpDefaultGameReply(player);
        reply.setMessage(fight.getResultOfFightForPlayer(player));
        return reply;
      }
      reply = setUpDefaultGameReply(oppositePlayer);
      reply.setMessage(fight.getResultOfFightForPlayer(oppositePlayer));
      webSocket.convertAndSend("/socket/start/" + oppositePlayer.getUsername(), reply);
      reply = setUpGameReplyForQuitPlayer(player);
      reply.setMessage(fight.getResultOfFightForPlayer(player));
      fightService.endFightInRoom(player.getWorldName(), player.getCurrentRoom().getRoomId());
      return reply;
    }
    reply = setUpDefaultGameReply(player);
    reply.setMessage("Waiting for the other player to choose...");
    reply.setFighting(true);
    return reply;
  }

  public GameReply quitPlayer(String username, String worldName) {
    PlayerInfo player = playerService.getPlayer(username);
    playerService.distributeGold(player);
    availableGames.get(worldName).createChest(player);
    availableGames.get(worldName).eliminatePlayer(player);
    playerService.getPlayersInWorld(worldName).stream()
        .filter(playerInfo -> !playerInfo.getUsername().equals(player.getUsername()))
        .forEach(
            playerInfo -> {
              GameReply reply = setUpDefaultGameReply(playerInfo);
              reply.setMessage(
                  username + " has quit! Some of their gold has been added to your inventory.");
              webSocket.convertAndSend("/socket/start/" + playerInfo.getUsername(), reply);
            });
    GameReply reply = setUpGameReplyForQuitPlayer(player);
    reply.setMessage("You quit the game!");
    return reply;
  }

  public void endGame(String worldName, String message) {
    playerService
        .getPlayersInWorld(worldName)
        .forEach(
            player -> {
              GameReply reply = setUpGameReplyForQuitPlayer(player);
              reply.setMessage(message);
              webSocket.convertAndSend("/socket/start/" + player.getUsername(), reply);
            });
    availableGames.remove(worldName);
    timerService.endTimerForGame(worldName);
    System.out.println("ended");
  }
}
